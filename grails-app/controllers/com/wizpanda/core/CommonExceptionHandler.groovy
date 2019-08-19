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

        // This is for backward compatibility
        //noinspection GroovyAssignabilityCheck
        errorResponse.put("errors", [[errorCode: e.errorCode, message: e.message, severity: "error", ttl: 10000]])

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
        Map errorResponse = [type: "validation-error", message: e.message]

        ObjectError fieldError = e.errors.getAllErrors()[0]
        errorResponse.code = fieldError.getCode()
        errorResponse.objectName = fieldError.getObjectName()

        if (fieldError instanceof FieldError) {
            errorResponse.field = fieldError.getField()
            errorResponse.rejectedValue = fieldError.getRejectedValue()
        }

        // This is for backward compatibility
        //noinspection GroovyAssignabilityCheck
        String message = messageSource.getMessage(fieldError, null)
        errorResponse.put("errors", [[message: message, severity: "error", ttl: 10000]])

        respond(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
