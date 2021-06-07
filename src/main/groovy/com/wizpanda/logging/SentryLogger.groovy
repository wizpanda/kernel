package com.wizpanda.logging

import com.wizpanda.utils.KernelRequestUtils
import com.wizpanda.utils.KernelUtils
import grails.util.Environment
import grails.util.Metadata
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import io.sentry.protocol.User
import org.grails.datastore.gorm.GormEntity

import javax.servlet.http.HttpServletRequest

/**
 * A class to log errors/warnings to Sentry for non-development environment.
 *
 * @author Shashank Agrawal
 */
@Slf4j
@CompileStatic
class SentryLogger {

    private static boolean useInLocalEnvironment = false

    static boolean shouldNotUseSentry() {
        if (KernelUtils.isLocalEnvironment()) {
            return !useInLocalEnvironment
        }

        false
    }

    static void setUseInLocalEnvironment(boolean shouldUse) {
        useInLocalEnvironment = shouldUse
    }

    static void init(String serverName, String dsn) {
        if (shouldNotUseSentry()) {
            return
        }

        println "Sentry init for $serverName"
        Metadata metadata = Metadata.current

        Sentry.init({ options ->
            options.setDsn(dsn)
            options.setEnvironment(Environment.current.name)
            options.setServerName(serverName)
            options.setRelease(metadata.getApplicationName() + "@" + metadata.getApplicationVersion())
            options.setTag("app_name", metadata.getApplicationName())
        })
    }

    static void capture(String message, Map<String, Object> extras = [:], SentryLevel level = SentryLevel.ERROR) {
        logInConsole(message, null, extras)

        if (shouldNotUseSentry()) {
            return
        }

        Message sentryMessage = new Message()
        sentryMessage.setFormatted(message)

        SentryEvent event = new SentryEvent()
        event.setMessage(sentryMessage)
        event.setLevel(level)

        extras?.each { String key, def value ->
            event.setExtra(key, value)
        }

        appendRequestToEvent(event)

        Sentry.captureEvent(event)
    }

    static void capture(Throwable throwable, Map<String, Object> extras = [:]) {
        if (shouldNotUseSentry()) {
            return
        }

        capture(throwable.getMessage(), throwable, extras)
    }

    /**
     *
     * @param gormEntity
     */
    static void captureValidationError(GormEntity gormEntity, String message = null) {
        if (shouldNotUseSentry()) {
            return
        }

        capture("Validation failed for $gormEntity", [errors: gormEntity.errors, message: message])
    }

    private static void logInConsole(String message, Throwable throwable, Map<String, Object> extras = [:]) {
        if (shouldNotUseSentry()) {
            log.error "$message, extras: $extras", throwable
            return
        }

        if (!throwable || (message == throwable.getMessage())) {
            log.error "Exception occurred: $message, extras: $extras"
        } else {
            log.error "$message, exception occurred: $throwable, extras: $extras"
        }
    }

    static void capture(String message, Throwable throwable, Map<String, Object> extras = [:]) {
        logInConsole(message, throwable, extras)

        if (shouldNotUseSentry()) {
            return
        }

        Message sentryMessage = new Message()
        sentryMessage.setFormatted(message)

        SentryEvent event = new SentryEvent(throwable)
        event.setMessage(sentryMessage)
        event.setLevel(SentryLevel.ERROR)

        extras?.each { String key, def value ->
            event.setExtra(key, value)
        }

        appendRequestToEvent(event)

        Sentry.captureEvent(event)
    }

    /**
     * Log the current request, if any to the given eventBuilder
     */
    private static void appendRequestToEvent(SentryEvent event) {
        HttpServletRequest request = KernelRequestUtils.getCurrentRequest()
        if (!request) {
            return
        }

        User user = new User()
        user.setIpAddress(KernelRequestUtils.getIPAddress(request))
        event.setUser(user)
    }
}
