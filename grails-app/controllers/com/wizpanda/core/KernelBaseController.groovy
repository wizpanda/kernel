package com.wizpanda.core

import grails.converters.JSON
import grails.gorm.PagedResultList
import org.grails.web.converters.exceptions.ConverterException
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus

/**
 * An abstract class which can be extended by any Grails controller class for providing some commonly used methods to deal with request &
 * response.
 *
 * @author Shashank Agrawal
 */
abstract class KernelBaseController {

    static responseFormats = ["json"]

    protected def respond(PagedResultList instanceList, Map result = [:]) {
        result.items = instanceList
        result.totalItems = instanceList.getTotalCount()
        respond(result)
    }

    protected def respond(Map data, HttpStatus status) {
        response.status = (status ?: HttpStatus.OK).value()
        render(data as JSON)
    }

    protected def respondOk() {
        render(status: HttpStatus.OK)
    }

    protected def notAcceptable(Map data) {
        respond(data, HttpStatus.NOT_ACCEPTABLE)
    }

    protected def reject(Map data) {
        respond(data, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    protected JSONObject requestData() {
        (JSONObject) request.JSON
    }

    protected JSONObject parseFilters(String parameterName = "filters") {
        String rawFilters = params.get(parameterName)
        if (!rawFilters) {
            return new JSONObject()
        }

        try {
            return JSON.parse(rawFilters) as JSONObject
        } catch (ConverterException e) {
            throw e
        }
    }
}
