package com.wizpanda.exception

import groovy.transform.CompileStatic

/**
 * An exception class which states that data received is
 * not valid or is missing some required information.
 * 
 * @author Shashank Agrawal
 */
@CompileStatic
class InvalidDataException extends ErrorCodeAwareException {

    InvalidDataException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }

    InvalidDataException(String message, Throwable cause = null) {
        super(message, cause)
    }
}
