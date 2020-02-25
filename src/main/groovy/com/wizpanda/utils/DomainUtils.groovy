package com.wizpanda.utils

import ch.qos.logback.classic.Logger
import grails.compiler.GrailsCompileStatic
import grails.validation.ValidationException
import grails.web.databinding.DataBindingUtils
import org.grails.datastore.gorm.GormEntity
import org.springframework.validation.BindingResult

/**
 * @author Shashank Agrawal
 * @since 0.0.1
 */
@GrailsCompileStatic
class DomainUtils {

    /**
     * Simple wrapper around {@link DataBindingUtils#bindObjectToInstance(java.lang.Object, java.lang.Object)} to provide default null
     * values to other parameters.
     *
     * @param object The object to bind to
     * @param source The source object
     * @param include The list of properties to include
     * @param exclude The list of properties to exclude
     * @param filter The prefix to filter by
     * @return A BindingResult if there were errors or null if it was successful
     */
    static BindingResult bind(Object object, Object source, List include = null, List exclude = null, String filter = null) {
        DataBindingUtils.bindObjectToInstance(object, source, include, exclude, filter)
    }

    /**
     * This is to support Grails version 3.2.0 or higher since the SLF4J is now default logger in Grails 3.2.0
     */
    static boolean save(GormEntity domainInstance, boolean flush, def log) {
        save(domainInstance, [flush: flush], log)
    }

    static boolean saveWithFailOnError(GormEntity domainInstance, boolean flush, def log) {
        save(domainInstance, [flush: flush, failOnError: true], log)
    }

    static boolean save(GormEntity domainInstance, Map params, def log) {
        if (!domainInstance) {
            return false
        }

        domainInstance.validate()

        if (domainInstance.hasErrors()) {
            // IntelliJ shows error when we use this class in the method arguments. So using this as workaround
            if (log instanceof Logger) {
                log.warn "Error saving $domainInstance $domainInstance.errors"
            }

            if (params.failOnError) {
                throw new ValidationException("Validation error occurred during call to save()", domainInstance.errors)
            }

            return false
        }

        domainInstance.save(params)

        return true
    }
}
