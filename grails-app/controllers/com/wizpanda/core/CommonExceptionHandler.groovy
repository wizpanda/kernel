package com.wizpanda.core

import com.wizpanda.exception.InvalidDataException
import com.wizpanda.exception.NotAcceptableException
import com.wizpanda.exception.NotFoundException
import com.wizpanda.exception.OperationFailedException
import org.springframework.http.HttpStatus

trait CommonExceptionHandler extends BaseController {

    private def respondException(Exception e, HttpStatus status) {
        respond([errors: [[message: e.message, severity: "error", ttl: 10000]]], status)
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
