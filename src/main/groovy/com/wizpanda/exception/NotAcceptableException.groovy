package com.wizpanda.exception

// See CommonExceptionHandler.groovy for further usage
class NotAcceptableException extends Exception {

    NotAcceptableException(String message = "", Throwable cause = null) {
        super(message, cause)
    }
}
