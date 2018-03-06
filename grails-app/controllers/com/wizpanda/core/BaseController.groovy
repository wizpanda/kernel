package com.wizpanda.core

import grails.converters.JSON
import grails.gorm.PagedResultList
import org.springframework.http.HttpStatus

trait BaseController {

    static responseFormats = ["json"]

    void respondListData(PagedResultList instanceList) {
        response.setHeader("total-count", instanceList.totalCount.toString())
        respond(instanceList)
    }

    def respond(Map data, HttpStatus status) {
        response.status = (status ?: HttpStatus.OK).value()
        render(data as JSON)
    }
}
