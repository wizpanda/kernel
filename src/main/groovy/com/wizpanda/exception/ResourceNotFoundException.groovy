package com.wizpanda.exception

/**
 * Throw when certain domain record does not found.
 * 
 * @author Shashank Agrawal
 */
class ResourceNotFoundException extends ErrorCodeAwareException {

    ResourceNotFoundException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }
}
