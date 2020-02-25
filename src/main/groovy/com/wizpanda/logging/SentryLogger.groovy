package com.wizpanda.logging

import com.wizpanda.utils.KernelUtils
import com.wizpanda.utils.RequestUtils
import grails.util.Environment
import grails.util.Metadata
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.sentry.Sentry
import io.sentry.SentryClient
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import io.sentry.event.interfaces.ExceptionInterface
import io.sentry.event.interfaces.HttpInterface
import io.sentry.event.interfaces.UserInterface
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

    private static SentryClient sentryClient
    private static Closure userInterfaceGenerator
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

        sentryClient = Sentry.init(dsn)
        sentryClient.setEnvironment(Environment.current.name)
        sentryClient.setServerName(serverName)

        Metadata metadata = Metadata.current
        sentryClient.setRelease(metadata.getApplicationVersion())
        sentryClient.addTag("app_name", metadata.getApplicationName())
    }

    static void setupUserInterfaceGenerator(Closure closure) {
        userInterfaceGenerator = closure
    }

    static void capture(String message, Map<String, Object> extras = [:], Event.Level level = Event.Level.ERROR) {
        logInConsole(message, null, extras)

        if (shouldNotUseSentry()) {
            return
        }

        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(message)
                .withLevel(level)

        extras?.each { String key, def value ->
            eventBuilder.withExtra(key, value)
        }

        appendRequestToLog(eventBuilder)

        Sentry.capture(eventBuilder)
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

        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(message)
                .withLevel(Event.Level.ERROR)
                .withSentryInterface(new ExceptionInterface(throwable))

        extras?.each { String key, def value ->
            eventBuilder.withExtra(key, value)
        }

        appendRequestToLog(eventBuilder)

        Sentry.capture(eventBuilder)
    }

    /**
     * Log the current request, if any to the given eventBuilder
     */
    private static void appendRequestToLog(EventBuilder eventBuilder) {
        HttpServletRequest request = RequestUtils.getCurrentRequest()
        if (!request) {
            return
        }

        UserInterface userInterface = createUserInterface(request)
        eventBuilder.withSentryInterface(userInterface, true)

        HttpInterface httpInterface = new HttpInterface(request, KernelRemoteAddressResolver.instance)
        eventBuilder.withSentryInterface(httpInterface, true)
    }

    private static UserInterface createUserInterface(HttpServletRequest request) {
        if (userInterfaceGenerator) {
            return userInterfaceGenerator.call(request) as UserInterface
        }

        String ipAddress = RequestUtils.getIPAddress(request)

        new UserInterface("", "", ipAddress, "", null)
    }
}
