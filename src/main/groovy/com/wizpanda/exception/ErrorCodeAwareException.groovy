package com.wizpanda.exception

abstract class ErrorCodeAwareException extends RuntimeException {

    Integer errorCode
    Map additionalData
    Integer httpResponseCode

    ErrorCodeAwareException(String message = null, Throwable cause = null) {
        super(message, cause)
    }
}
