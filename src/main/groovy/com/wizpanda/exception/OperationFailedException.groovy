package com.wizpanda.exception

/**
 * Used to throw when an operation failed because of some exception or some falsy data.
 * 
 * @author Shashank Agrawal
 */
class OperationFailedException extends ErrorCodeAwareException {

    OperationFailedException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }
}
