package com.vasu.manzana;

import java.util.ArrayList;
import java.util.List;

public class ExecQueryResponse extends Response {
	
	private List<String> columns;
	private List<List<String>>rows;
	public ExecQueryResponse() {
		super();
		rows = new ArrayList<List<String>>();
		columns = new ArrayList<String>();
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<List<String>> getRows() {
		return rows;
	}
	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}
	
}
