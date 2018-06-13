package com.wizpanda.exception

// See CommonExceptionHandler.groovy for further usage
class NotAcceptableException extends ErrorCodeAwareException {

    NotAcceptableException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }
}
