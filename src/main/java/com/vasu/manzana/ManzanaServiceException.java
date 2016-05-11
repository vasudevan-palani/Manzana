package com.vasu.manzana;

public class ManzanaServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;

	private String errorMsg;

	
	
	public String getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getErrorMsg() {
		return errorMsg;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}



	public ManzanaServiceException(String errorCode,String errorMsg) {
		this.errorCode = errorCode;
		this.setErrorMsg(errorMsg);
	}



	public ManzanaServiceException(String errorCode) {
		this.errorCode = errorCode;
	}

}
