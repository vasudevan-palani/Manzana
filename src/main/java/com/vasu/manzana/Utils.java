package com.vasu.manzana;

public class Utils {

	public static void setError(ErrorMsg errorMsg, Response response, String queryAlreadyExists) {
		// TODO Auto-generated method stub
		response.setErrorMsg(errorMsg.getMsg(queryAlreadyExists));
		response.setErrorcode(queryAlreadyExists);
	}

}
