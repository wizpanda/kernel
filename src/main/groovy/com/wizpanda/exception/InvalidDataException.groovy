package com.wizpanda.exception

/**
 * An exception class which states that data received is
 * not valid or is missing some required information.
 * 
 * @author Shashank Agrawal
 */
class InvalidDataException extends RuntimeException {

    InvalidDataException(String message = "", Throwable cause = null) {
        super(message, cause)
    }
}