package com.wizpanda.exception

/**
 * Used to throw when an operation failed because of some exception or some falsy data.
 * 
 * @author Shashank Agrawal
 */
class OperationFailedException extends Exception {

    OperationFailedException(String message = "", Throwable cause = null) {
        super(message, cause)
    }
}
