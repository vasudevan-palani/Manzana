package com.vasu.manzana;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class ErrorMsg {

	public ErrorMsg() {
		try {
			errorMessages = PropertiesLoaderUtils.loadProperties(new ClassPathResource("errorMessages.properties"));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private Properties errorMessages;

	public String getMsg(String errorCode) {
		return errorMessages.getProperty(errorCode);
	}

}
