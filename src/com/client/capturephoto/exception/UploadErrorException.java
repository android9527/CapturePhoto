package com.client.capturephoto.exception;

public class UploadErrorException extends Exception {

	public UploadErrorException(Throwable t) {
		super(t);
	}

	public UploadErrorException(String detailMessage) {
		super(detailMessage);
	}
}