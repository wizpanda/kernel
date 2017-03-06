package com.wizpanda.utils

import javax.servlet.http.HttpServletRequest

/**
 * Created by shashank on 06/03/17.
 *
 * @since 1.0.5
 */
class RequestUtils {

    static Map getHeaders(HttpServletRequest request) {
        return request.headerNames.toList().collectEntries {
            return [(it): request.getHeader(it)]
        }
    }
}
