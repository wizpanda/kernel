package com.wizpanda.utils.date

class DateRangeUtils {

    private Calendar calendar = Calendar.getInstance()

    private void setBeginningOfDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private void setEndOfDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    DateRange getDateRangeForToday() {
        setBeginningOfDay()
        Date start = calendar.getTime()

        setEndOfDay()
        Date end = calendar.getTime()

        new DateRange([start: start, end: end])
    }

    DateRange getDateRangeForThisWeek() {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        setBeginningOfDay()
        Date monday = calendar.getTime()

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        setEndOfDay()
        Date sunday = calendar.getTime() + 1

        new DateRange([start: monday, end: sunday])
    }

    DateRange getDateRangeForThisMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        setBeginningOfDay()
        Date firstDayOfTheMonth = calendar.getTime()

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        setEndOfDay()
        Date lastDayOfTheMonth = calendar.getTime()

        new DateRange([start: firstDayOfTheMonth, end: lastDayOfTheMonth])
    }

    // TODO check for JodaTime alternative.
    /*static DateRange getRange(def rawStartDate, def rawEndDate) {
        if (!rawStartDate) {
            return null
        }

        Date startDate, endDate
        JodaUtils utils = new JodaUtils().detectTimeZone()

        if (rawStartDate instanceof Date) {
            startDate = rawStartDate
        } else if (rawStartDate instanceof String) {
            startDate = utils.parse(rawStartDate)
        } else {
            throw new IllegalArgumentException("Raw start date should either be a String or Date")
        }

        if (rawEndDate) {
            if (rawEndDate instanceof Date) {
                endDate = rawEndDate
            } else if (rawEndDate instanceof String) {
                endDate = DateUtils.getEndOfDay(utils.parse(rawEndDate))
            } else {
                throw new IllegalArgumentException("Raw end date should either be a String or Date")
            }
        } else {
            endDate = DateUtils.getEndOfDay(startDate)
        }

        new DateRange([start: startDate, end: endDate])
    }*/
}

class DateRange {
    Date start
    Date end
}
