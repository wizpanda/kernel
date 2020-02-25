package com.wizpanda.exception

import groovy.transform.CompileStatic

@CompileStatic
// See CommonExceptionHandler.groovy for further usage
class NotAcceptableException extends ErrorCodeAwareException {

    NotAcceptableException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }

    NotAcceptableException(String message, Throwable cause = null) {
        super(message, cause)
    }
}
