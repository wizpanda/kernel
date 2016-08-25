package com.wizpanda.utils.date

import grails.util.Holders
import groovy.time.TimeCategory

import java.text.SimpleDateFormat

class DateUtils {

    static SimpleDateFormat dateParser = new SimpleDateFormat(Holders.getConfig()["grails.databinding.dateFormats"][0])

    /**
     * "between" query in Grails criteria uses inclusive of both the dates. So when we have to get some database
     * records which are created on suppose 31st Mar 2016 then we have to write something like this:
     *
     * <code>
     *     Date dateCreatedStart = new Date("03/31/2016")
     *     Date dateCreatedEnd = dateCreatedStart + 1
     *
     *     User.withCriteria {
     *          between("dateCreated", dateCreated, dateCreatedEnd)
     *     }
     * </code>
     *
     * Doing this will get all user's who are created at 31st March but will be also get those user's who are
     * registered on sharp 1st Apr 2016 00:00 due to inclusive of end date. So this method will get the end date by
     * incrementing the day by one by decrementing one millisecond just to complete one exact date range.
     *
     * @param date
     * @return
     */
    static Date getEndOfDay(Date date) {
        Date endDate
        use(TimeCategory) {
            endDate = date + 1.day
            endDate = endDate - 1.millisecond
        }

        endDate
    }
}
