package com.wizpanda.exception

import groovy.transform.CompileStatic

/**
 * Throw when certain domain record does not found.
 * 
 * @author Shashank Agrawal
 */
@CompileStatic
class ResourceNotFoundException extends ErrorCodeAwareException {

    ResourceNotFoundException(Integer errorCode = null, String message = null, Throwable cause = null) {
        super(message, cause)
        this.errorCode = errorCode
    }

    ResourceNotFoundException(String message, Throwable cause = null) {
        super(message, cause)
    }
}
