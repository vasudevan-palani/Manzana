package com.vasu.manzana;

import java.util.List;

public class ExecQueryRequest {
	private Integer queryId;
	private String env;
	private List<NameValue> params;


	public Integer getQueryId() {
		return queryId;
	}

	public void setQueryId(Integer queryId) {
		this.queryId = queryId;
	}

	public List<NameValue> getParams() {
		return params;
	}

	public void setParams(List<NameValue> params) {
		this.params = params;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}




	
}

