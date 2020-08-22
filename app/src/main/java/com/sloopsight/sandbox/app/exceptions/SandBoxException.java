package com.sloopsight.sandbox.app.exceptions;

public abstract class SandBoxException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SandBoxException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SandBoxException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SandBoxException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
