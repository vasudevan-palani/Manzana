package com.vasu.manzana;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vasu.manzana.model.Query;
import com.vasu.manzana.model.QueryNickName;
import com.vasu.manzana.repo.QueryNickNameRepository;
import com.vasu.manzana.repo.QueryRepository;

@RestController
public class HelloController {

	@Autowired
	@Qualifier(value = "clfysit1")
	JdbcTemplate clfysit1;

	@Autowired
	@Qualifier(value = "clfysci")
	JdbcTemplate clfysci;

	@Autowired
	@Qualifier(value = "manzana")
	JdbcTemplate manzana;

	@Autowired
	QueryRepository queryRepo;

	@Autowired
	QueryNickNameRepository queryNickNameRepo;

	@Autowired
	ErrorMsg errorMsg;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Manzana!";
	}

	@RequestMapping(produces = "application/json", value = "/hello", method = { RequestMethod.POST,
			RequestMethod.OPTIONS })
	public ResponseEntity<Response> hello(@RequestBody Request request) {

		Response response = new Response();
		response.setErrorMsg("Hello " + request.getName() + "!!");

		// List<HashMap<String, Object>> rows = manzana.query(
		// "SELECT id, first_name, last_name FROM users where first_name=?", new
		// HashMapRowMapper(), "Vasudevan");
		// rows = clfysci.query("select * from x_program_parameters where
		// x_program_name=?", new HashMapRowMapper(),
		// "Lifeline - CA - BUNL1");
		// System.out.println(rows.size());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(produces = "application/json", value = "/addQuery", method = { RequestMethod.POST,
			RequestMethod.OPTIONS })
	public ResponseEntity<Response> addQuery(@RequestBody AddQueryRequest request) {

		Response response = new Response();

		Query query = queryRepo.findByName(request.getName());

		if (query != null) {
			Utils.setError(errorMsg, response, ErrorCodes.QUERY_ALREADY_EXISTS);
		} else {
			query = new Query();
			query.setName(request.getName());
			query.setQuery(request.getQuery());
			queryRepo.save(query);

			String[] nickNames = request.getNickNames().split(",");
			for (int i = 0; i < nickNames.length; i++) {
				String nickName = nickNames[i].trim();
				QueryNickName queryNickName = new QueryNickName();
				queryNickName.setName(nickName);
				queryNickName.setQueryId(query.getId());
				queryNickNameRepo.save(queryNickName);
			}
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(produces = "application/json", value = "/getQueryNames", method = { RequestMethod.POST,
			RequestMethod.OPTIONS })
	public ResponseEntity<Response> getQueryNames(@RequestBody AddQueryRequest request) {

		Response response = new Response();
		ArrayList<SelectItem> names = new ArrayList<SelectItem>();

		Iterable<Query> queryList = queryRepo.findAll();

		for (Query query : queryList) {
			SelectItem item = new SelectItem();
			item.setId(query.getId() + "");
			item.setName(query.getName());
			names.add(item);
		}
		response.setObject(names);

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(produces = "application/json", value = "/execQuery", method = { RequestMethod.POST,
			RequestMethod.OPTIONS })
	@Transactional
	public ResponseEntity<Response> execQuery(@RequestBody ExecQueryRequest request) {

		ExecQueryResponse response = new ExecQueryResponse();

		try {
			
			if(request.getEnv() == null || request.getEnv().equalsIgnoreCase("")){
				throw new ManzanaServiceException(ErrorCodes.INVALID_ENV);
			}
			
			JdbcTemplate jdbc = getTemplate(request.getEnv());
			
			if(jdbc == null){
				throw new ManzanaServiceException(ErrorCodes.INVALID_ENV);
			}
			
			Query query = queryRepo.findById(request.getQueryId());
			List<RowHolder> rows = null;
			if (query != null) {

				String queryString = query.getQuery();

				List<NameValue> nameValues = request.getParams();

				ArrayList<String> params = getParams(queryString);

				for (String param : params) {
					boolean found = false;
					System.out.println("Param : "+param);
					for (NameValue name : nameValues) {
						System.out.println("Name:"+name.getName());
						
						if (name.getName().equalsIgnoreCase(param)) {
							found = true;
						}
					}
					if (found == false) {
						throw new ManzanaServiceException(ErrorCodes.INVALID_PARAMS, params.toString());
					}
				}

				for (NameValue nameValue : nameValues) {
					queryString = queryString.replaceAll("\\{" + nameValue.getName() + "\\}",
							"'" + nameValue.getValue() + "'");
				}

				jdbc.execute("ALTER SESSION SET NLS_DATE_FORMAT='yyyy-mm-dd hh24:mi:ss'");
				rows = jdbc.query(queryString, new HashMapRowMapper());
			}
			if (rows.size() > 0) {
				response.setColumns(rows.get(0).getColumns());
			}

			for (RowHolder rowHolder : rows) {
				response.getRows().add(rowHolder.getRow());
			}

		} catch (ManzanaServiceException e) {
			Utils.setError(errorMsg, response, e.getErrorCode());
			response.setErrorMsg(response.getErrorMsg() + "<br>" + e.getErrorMsg());
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private JdbcTemplate getTemplate(String env) {
		JdbcTemplate jdbc = null;
		
		HashMap<String,JdbcTemplate> map = new HashMap<String, JdbcTemplate>();
		
		map.put("clfysci", this.clfysci);
		map.put("clfysit1", this.clfysit1);
		
		jdbc = map.get(env);
		
		return jdbc;
	}

	private ArrayList<String> getParams(String query) {
		ArrayList<String> params = new ArrayList<String>();
		Pattern p = Pattern.compile("\\{\\S*\\}");
		Matcher m = p.matcher(query); // get a matcher object
		int count = 0;

		while (m.find()) {
			count++;
			System.out.println("Match number " + count);
			System.out.println("start(): " + m.start());
			System.out.println("end(): " + m.end());
			params.add(query.substring(m.start() + 1, m.end() - 1));
			System.out.println(query.substring(m.start() + 1, m.end() - 1));
		}
		return params;
	}

	private static final class HashMapRowMapper implements RowMapper<RowHolder> {

		public RowHolder mapRow(ResultSet rs, int rowNum) throws SQLException {
			RowHolder row = new RowHolder();
			ResultSetMetaData meta = rs.getMetaData();
			final int columnCount = meta.getColumnCount();

			for (int column = 1; column <= columnCount; column++) {
				Object value = rs.getObject(column);

				row.getColumns().add(meta.getColumnLabel(column));
				if (value != null) {
					if (value instanceof Timestamp || value instanceof java.sql.Date) {
						System.out.println(rs.getTimestamp(column));
						row.getRow().add(rs.getTimestamp(column).toString());
					} else {
						row.getRow().add(value.toString());
					}

				} else {
					row.getRow().add("");
				}
			}
			return row;
		}
	}

}