package com.wizpanda.core

import grails.converters.JSON
import grails.gorm.PagedResultList
import org.springframework.http.HttpStatus

trait BaseController {

    static responseFormats = ["json"]

    def respond(PagedResultList instanceList, Map result = [:]) {
        result.items = instanceList
        result.totalItems = instanceList.getTotalCount()
        respond(result)
    }

    def respond(Map data, HttpStatus status) {
        response.status = (status ?: HttpStatus.OK).value()
        render(data as JSON)
    }
}
