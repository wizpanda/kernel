package com.wizpanda.logging

import com.wizpanda.utils.KernelUtils
import com.wizpanda.utils.RequestUtils
import grails.util.Environment
import grails.util.Metadata
import groovy.util.logging.Slf4j
import io.sentry.Sentry
import io.sentry.SentryClient
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import io.sentry.event.interfaces.ExceptionInterface
import io.sentry.event.interfaces.HttpInterface
import io.sentry.event.interfaces.UserInterface

import javax.servlet.http.HttpServletRequest

/**
 * A class to log errors/warnings to Sentry for non-development environment.
 *
 * @author Shashank Agrawal
 */
@Slf4j
class SentryLogger {

    private static SentryClient sentryClient
    private static Closure userInterfaceGenerator

    static void init(String serverName, String dsn) {
        if (KernelUtils.isLocalEnvironment()) {
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

        if (KernelUtils.isLocalEnvironment()) {
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
        if (KernelUtils.isLocalEnvironment()) {
            return
        }

        capture(throwable.getMessage(), throwable, extras)
    }

    private static void logInConsole(String message, Throwable throwable, Map<String, Object> extras = [:]) {
        if (KernelUtils.isLocalEnvironment()) {
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

        if (KernelUtils.isLocalEnvironment()) {
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
