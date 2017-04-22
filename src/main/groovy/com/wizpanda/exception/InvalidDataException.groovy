package com.wizpanda.exception

/**
 * An exception class which states that data received is
 * not valid or is missing some required information.
 * 
 * @author Shashank Agrawal
 */
class InvalidDataException extends Exception {

    InvalidDataException(String message = "") {
        super(message)
    }
}