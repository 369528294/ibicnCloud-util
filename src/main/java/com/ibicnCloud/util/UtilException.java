package com.ibicnCloud.util;

public class UtilException extends Exception {
	private static final long serialVersionUID = 1339439433313285858L;

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
