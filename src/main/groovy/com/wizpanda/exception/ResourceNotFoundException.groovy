package com.wizpanda.exception

/**
 * Throw when certain domain record does not found.
 * 
 * @author Shashank Agrawal
 */
class ResourceNotFoundException extends RuntimeException {

    ResourceNotFoundException(String message = "", Throwable cause = null) {
        super(message, cause)
    }
}
