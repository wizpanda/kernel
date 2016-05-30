package com.wizpanda.utils

import org.apache.commons.logging.Log

/**
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class DomainUtils {

    static boolean save(Object domainInstance, boolean flush, Log log) {
        if (!domainInstance) {
            return false
        }

        domainInstance.validate()

        if (domainInstance.hasErrors()) {
            log.warn "Error saving $domainInstance $domainInstance.errors"
            return false
        }

        domainInstance.save(flush: flush)

        return true
    }
}