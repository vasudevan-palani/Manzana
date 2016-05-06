package com.vasu.manzana;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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

@RestController
public class HelloController {

	@Autowired
	@Qualifier(value="clfysci")
	JdbcTemplate clfysci;
	
	@Autowired
	@Qualifier(value="manzana")
	JdbcTemplate manzana;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Manzana!";
	}

	@RequestMapping(produces = "application/json", value = "/hello", method = { RequestMethod.POST,
			RequestMethod.OPTIONS })
	public ResponseEntity<Response> hello(@RequestBody Request request) {
		
		Response response = new Response();
		response.setErrorMsg("Hello " + request.getName() + "!!");

		List<HashMap<String, Object>> rows = manzana.query(
				"SELECT id, first_name, last_name FROM users where first_name=?", new HashMapRowMapper(), "Vasudevan");
		rows = clfysci.query(
				"select * from x_program_parameters where x_program_name=?", new HashMapRowMapper(), "Lifeline - CA - BUNL1");
		System.out.println(rows.size());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private static final class HashMapRowMapper implements RowMapper<HashMap<String, Object>> {

		public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			HashMap<String, Object> row = new HashMap<String, Object>();
			ResultSetMetaData meta = rs.getMetaData();
			final int columnCount = meta.getColumnCount();

			for (int column = 1; column <= columnCount; column++) {
				Object value = rs.getObject(column);
				if (value != null) {
					row.put(meta.getColumnLabel(column), value.toString());
				} else {
					row.put(meta.getColumnLabel(column), "");
				}
			}
			System.out.println(row.toString());
			return row;
		}
	}

}