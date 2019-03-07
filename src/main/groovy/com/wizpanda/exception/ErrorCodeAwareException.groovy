package com.wizpanda.exception

abstract class ErrorCodeAwareException extends RuntimeException {

    Integer errorCode
    Map additionalData

    ErrorCodeAwareException(String message = null, Throwable cause = null) {
        super(message, cause)
    }
}