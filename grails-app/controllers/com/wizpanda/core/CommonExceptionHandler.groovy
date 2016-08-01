package com.wizpanda.core

import com.wizpanda.exception.NotAcceptableException
import org.springframework.http.HttpStatus

trait CommonExceptionHandler {

    static responseFormats = ["json"]

    def handleInvalidDataException(NotAcceptableException e) {
        respond([status: HttpStatus.NOT_ACCEPTABLE], [errors: [[message: e.message, severity: "error", ttl: 10000]]])
    }
}
