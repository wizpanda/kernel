package com.wizpanda.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

/**
 * A logback logging filter only used in development for filtering our some messages for hibernate.
 *
 * @author Shashank Agrawal
 */
class DevelopmentLoggingFilter extends Filter<ILoggingEvent> {

    @Override
    FilterReply decide(ILoggingEvent event) {
        String logger = event.getLoggerName()
        String message = event.getMessage()

        if (logger == "org.hibernate.internal.SessionImpl") {
            if (message.startsWith("Opened session at timestamp") || message == "Closing session") {
                return FilterReply.NEUTRAL
            }

            return FilterReply.DENY

        } else if (logger == "org.hibernate.event.internal.AbstractFlushingEventListener") {
            if (message == "Flushing session" || message.startsWith("Flushed: ")) {
                return FilterReply.NEUTRAL
            }

            return FilterReply.DENY
        }

        return FilterReply.ACCEPT
    }
}