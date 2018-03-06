package com.wizpanda.core

import com.wizpanda.exception.InvalidDataException
import com.wizpanda.exception.NotAcceptableException
import com.wizpanda.exception.OperationFailedException
import com.wizpanda.exception.ResourceNotFoundException
import grails.validation.ValidationException
import org.springframework.http.HttpStatus

trait CommonExceptionHandler extends BaseController {

    private def respondException(Exception e, HttpStatus status) {
        respond([errors: [[message: e.message, severity: "error", ttl: 10000]]], status)
    }

    def handleNotAcceptableException(NotAcceptableException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleInvalidDataException(InvalidDataException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleOperationFailedException(OperationFailedException e) {
        respondException(e, HttpStatus.NOT_ACCEPTABLE)
    }

    def handleResourceNotFoundException(ResourceNotFoundException e) {
        respondException(e, HttpStatus.NOT_FOUND)
    }

    def handleValidtionException(ValidationException e) {
        respondException(e, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
