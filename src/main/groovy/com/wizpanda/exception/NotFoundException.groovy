package com.wizpanda.exception

/**
 * A exception class used to represent a state where a expected
 * record does not found either in database or some where else.
 * @author Shashank Agrawal
 */
class NotFoundException extends Exception {

    NotFoundException(String message) {
        super(message)
    }
}