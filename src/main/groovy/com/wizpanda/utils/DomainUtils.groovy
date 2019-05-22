package com.wizpanda.utils

import grails.validation.ValidationException
import org.apache.commons.logging.Log
import org.grails.datastore.gorm.GormEntity
import org.slf4j.Logger

/**
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class DomainUtils {

    /**
     * This is to support older version of Grails before 3.2.0 since the log property injected at compile time
     * into all classes is now an Slf4j Logger instance and not an instance of the Commons Logging Log class
     */
    static boolean save(GormEntity domainInstance, boolean flush, Log log) {
        save(domainInstance, [flush: flush], log)
    }

    /**
     * This is to support Grails version 3.2.0 or higher since the SLF4J is now default logger in Grails 3.2.0
     */
    static boolean save(GormEntity domainInstance, boolean flush, Logger log) {
        save(domainInstance, [flush: flush], log)
    }

    static boolean saveWithFailOnError(GormEntity domainInstance, boolean flush, Logger log) {
        save(domainInstance, [flush: flush, failOnError: true], log)
    }

    static boolean save(GormEntity domainInstance, Map params, def log) {
        if (!domainInstance) {
            return false
        }

        domainInstance.validate()

        if (domainInstance.hasErrors()) {
            log.warn "Error saving $domainInstance $domainInstance.errors"

            if (params.failOnError) {
                throw new ValidationException("Validation error occurred during call to save()", domainInstance.errors)
            }

            return false
        }

        domainInstance.save(params)

        return true
    }
}