package com.wizpanda.utils.date

import grails.util.Holders
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletWebRequest

/**
 * Utility class and wrapper for Joda-Time library.
 * @author Shashank Agrawal
 */
class JodaUtils {

    /**
     * Name of the request header which will have the current user's timezone.
     * Check https://github.com/sagrawal14/angular-server-timezone for example.
     */
    private static final String TIMEZONE_KEY_NAME = "User-Time-Zone"
    private static Log log = LogFactory.getLog(this)
    private static DateTimeZone defaultTimeZone = DateTimeZone.UTC

    private String pattern
    private DateTimeZone timeZone

    static setDefaultTimeZone(DateTimeZone defaultTZ) {
        if (!defaultTZ) {
            throw new IllegalArgumentException("Provide a default time zone")
        }
        defaultTimeZone = defaultTZ
    }

    /**
     * This method reads a request header in current Grails request (if any) which will have user's current time zone.
     * If no time zone found in the request header then this method looks up the same key in the user's session.
     * @return this to allow chaining
     */
    JodaUtils detectTimeZone() {
        ServletWebRequest currentRequest

        try {
            currentRequest = RequestContextHolder.currentRequestAttributes()
        } catch (IllegalStateException e) {
            // If called from a code which is outside of Grails web request (like Job, Thread)
            return this
        }

        String timeZoneID = currentRequest.getHeader(TIMEZONE_KEY_NAME)
        if (!timeZoneID) {
            timeZoneID = currentRequest.getSession()[TIMEZONE_KEY_NAME]
        }

        validateTimeZoneID(timeZoneID)
        this
    }

    private void validateTimeZoneID(String timeZoneID) {
        if (!timeZoneID) {
            return
        }

        try {
            timeZone = DateTimeZone.forID(timeZoneID)
        } catch (IllegalArgumentException e) {
            log.error("Invalid TimeZone ID", e)
        }
    }

    JodaUtils withPattern(String pattern) {
        this.pattern = pattern
        this
    }

    /**
     * Set time zone. Any valid TimeZone string which may be interpreted by {@link org.joda.time.DateTimeZone DateTimeZone}
     *
     * @see "http://www.joda.org/joda-time/apidocs/org/joda/time/DateTimeZone.html"
     * @param timeZoneID
     * @return this for chaining
     */
    JodaUtils withTimeZone(String timeZoneID) {
        validateTimeZoneID(timeZoneID)
        this
    }

    /**
     * Parse the given date string with the TimeZone (if any) and return the Date object which will be in UTC
     * since java.util.Date does not contain any TimeZone information.
     * @param dateTimeString
     */
    Date parse(String dateTimeString) {
        return parseAsDateTime(dateTimeString).toDate()
    }

    DateTime parseAsDateTime(String dateTimeString) {
        // If pattern is specified
        if (this.pattern) {
            // Then parse as that pattern only
            return parseAsDateTime(dateTimeString, this.pattern)
        }

        DateTime dateTime
        Exception firstException

        // Otherwise read from the global configuration for the list of pattern
        Holders.getConfig()["grails.databinding.dateFormats"].find { String pattern ->
            try {
                dateTime = parseAsDateTime(dateTimeString, pattern)
                return true     // break the loop
            } catch (IllegalArgumentException e) {
                firstException = firstException ?: e
                return false   // continue the loop
            }
        }

        if (!dateTime && firstException) {
            throw firstException
        }

        dateTime
    }

    DateTime parseAsDateTime(String dateTimeString, String pattern) {
        DateTimeFormat.forPattern(pattern).withZone(timeZone ?: defaultTimeZone).parseDateTime(dateTimeString)
    }
}
