package com.wizpanda.core

import com.wizpanda.exception.ErrorCodeAwareException
import com.wizpanda.exception.InvalidDataException
import com.wizpanda.exception.NotAcceptableException
import com.wizpanda.exception.OperationFailedException
import com.wizpanda.exception.ResourceNotFoundException
import grails.validation.ValidationException
import org.springframework.http.HttpStatus

trait CommonExceptionHandler extends BaseController {

    private def respondException(ErrorCodeAwareException e, HttpStatus status) {
        respond([errors: [[errorCode: e.errorCode, message: e.message, severity: "error", ttl: 10000]]], status)
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
        respond([errors: [[message: e.message, severity: "error", ttl: 10000]]], HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
