package com.wizpanda.exception

import com.wizpanda.logging.SentryLogger
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.web.errors.GrailsExceptionResolver
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.reflect.InvocationTargetException

/**
 * Global exception resolver to handle any exception and log it to Sentry via {@link com.wizpanda.logging.SentryLogger}.
 *
 * @author Shashank Agrawal
 * @since 3.0.1
 */
@Slf4j
@CompileStatic
class KernelExceptionResolver extends GrailsExceptionResolver {

    Exception findWrappedException(Exception e) {
        Exception exception = super.findWrappedException(e)

        if ((exception instanceof InvocationTargetException) && exception.cause) {
            return exception.cause as Exception
        }

        return e
    }

    @Override
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Exception exception = findWrappedException(ex)

        SentryLogger.capture(exception)

        super.resolveException(request, response, handler, ex)
    }
}
