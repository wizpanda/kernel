package com.wizpanda.core

import com.wizpanda.exception.InvalidDataException
import com.wizpanda.exception.NotAcceptableException
import com.wizpanda.exception.NotFoundException
import com.wizpanda.exception.OperationFailedException
import grails.converters.JSON
import org.springframework.http.HttpStatus

trait CommonExceptionHandler {

    static responseFormats = ["json"]

    private def respondException(Exception e, HttpStatus status) {
        response.status = status.value()
        render([errors: [[message: e.message, severity: "error", ttl: 10000]]] as JSON)
    }

    def handleInvalidDataException(NotAcceptableException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleInvalidDataException(InvalidDataException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleOperationFailedException(OperationFailedException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleNotFoundException(NotFoundException e) {
        respondException(e, HttpStatus.NOT_FOUND)
    }
}
