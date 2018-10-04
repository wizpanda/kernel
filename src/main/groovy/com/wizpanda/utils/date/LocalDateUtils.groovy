package com.wizpanda.utils.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * Utility class to perform local date related operations.
 *
 * @author Ankit Kumar Singh
 * @since 2.0.6
 */
class LocalDateUtils {

    static DateRange getDateRangeForToday(ZoneId zoneId) {
        // Determine today’s date as seen by the user's location (time zone).
        LocalDate startOfDay = LocalDate.now(zoneId)
        LocalDateTime endOfDay = startOfDay.atTime(LocalTime.MAX)

        DateRange dateRange = new DateRange()
        dateRange.start = toDate(startOfDay, zoneId)
        dateRange.end = toDate(endOfDay, zoneId)

        return dateRange
    }

    static DateRange getDateRangeForLast7Days(ZoneId zoneId) {
        // Determine current time as seen by the user's location (time zone).
        LocalDateTime now = LocalDateTime.now(zoneId)
        LocalDateTime last7thDay = now.minusDays(7).toLocalDate().atTime(LocalTime.MIDNIGHT)

        DateRange dateRange = new DateRange()
        dateRange.start = toDate(last7thDay, zoneId)
        dateRange.end = toDate(now, zoneId)

        return dateRange
    }

    static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        zoneId = zoneId ?: ZoneId.systemDefault()

        Date.from(localDateTime.atZone(zoneId).toInstant())
    }

    static Date toDate(LocalDate localDate, ZoneId zoneId) {
        zoneId = zoneId ?: ZoneId.systemDefault()

        Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant())
    }
}
