package com.wizpanda.exception

import groovy.transform.CompileStatic

/**
 * Used to throw when an operation failed because of some exception or some falsy data.
 * 
 * @author Shashank Agrawal
 */
@CompileStatic
class OperationFailedException extends ErrorCodeAwareException {

    OperationFailedException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }

    OperationFailedException(String message, Throwable cause = null) {
        super(message, cause)
    }
}
