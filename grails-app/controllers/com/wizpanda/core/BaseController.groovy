package com.wizpanda.core

import grails.gorm.PagedResultList

trait BaseController {

    void respondListData(PagedResultList instanceList) {
        response.setHeader("total-count", instanceList.totalCount.toString())
        respond(instanceList)
    }
}
