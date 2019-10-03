package com.wizpanda.core

import com.wizpanda.exception.ErrorCodeAwareException
import com.wizpanda.exception.InvalidDataException
import com.wizpanda.exception.NotAcceptableException
import com.wizpanda.exception.OperationFailedException
import com.wizpanda.exception.ResourceNotFoundException
import grails.validation.ValidationException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

trait CommonExceptionHandler extends BaseController {

    MessageSource messageSource

    def respondException(ErrorCodeAwareException e, HttpStatus status) {
        Map errorResponse = [code: e.errorCode, message: e.message]
        if (e.additionalData) {
            errorResponse.putAll(e.additionalData)
        }

        respond(errorResponse, status)
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

    def handleValidationException(ValidationException e) {
        ObjectError fieldError = e.errors.getAllErrors()[0]
        String message = messageSource.getMessage(fieldError, null)

        Map errorResponse = [
                message   : message,
                type      : "validation-error",
                code      : fieldError.getCode(),
                objectName: fieldError.getObjectName()
        ]

        if (fieldError instanceof FieldError) {
            errorResponse.field = fieldError.getField()
            errorResponse.rejectedValue = fieldError.getRejectedValue()
        }

        respond(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
