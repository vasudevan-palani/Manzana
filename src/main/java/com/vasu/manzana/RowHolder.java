package com.vasu.manzana;

import java.util.ArrayList;

public class RowHolder {
	
	public RowHolder() {
		columns = new ArrayList<String>();
		row = new ArrayList<String>();
	}
	
	private ArrayList<String> columns;
	private ArrayList<String> row;

	public ArrayList<String> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<String> columns) {
		this.columns = columns;
	}

	public ArrayList<String> getRow() {
		return row;
	}

	public void setRow(ArrayList<String> row) {
		this.row = row;
	}
}
