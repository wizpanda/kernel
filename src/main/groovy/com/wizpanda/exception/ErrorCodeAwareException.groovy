package com.wizpanda.exception

abstract class ErrorCodeAwareException extends RuntimeException {

    Integer errorCode

    ErrorCodeAwareException(String message = null, Throwable cause = null) {
        super(message, cause)
    }
}