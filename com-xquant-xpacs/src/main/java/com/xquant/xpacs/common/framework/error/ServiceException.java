package com.xquant.xpacs.common.framework.error;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -7680497118550281966L;

	public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
