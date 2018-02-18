package com.wizpanda.utils

import grails.util.Holders
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * Created by shashank on 06/03/17.
 *
 * @since 1.0.5
 * @author Shashank Agrawal
 */
class RequestUtils {

    private RequestUtils() {
        // Hide default constructor as this is a utility class
    }

    static HttpServletRequest getCurrentRequest() {
        try {
            return RequestContextHolder.currentRequestAttributes().request
        } catch (IllegalStateException e) {
            // We might be in a thread where there is no request
            return null
        }
    }

    static HttpSession getSession(boolean create = false) {
        getCurrentRequest()?.getSession(create)
    }
    
    static Map getHeaders(HttpServletRequest request) {
        return request.headerNames.toList().collectEntries {
            return [(it): request.getHeader(it)]
        }
    }

    static Map parseQueryString(String query) {
        if (!query) {
            return [:]
        }

        return query.split("&").collectEntries { param ->
            param.split("=", 2).collect { URLDecoder.decode(it, "UTF-8") }
        }
    }

    static String toQueryString(Map params) {
        return params.collect { key, value -> "$key=" + URLEncoder.encode(value?.toString(), "UTF-8") }.join("&")
    }

    static Map maskSensitiveData(Map params) {
        Map clonedParams = params.clone()
        Holders.getFlatConfig()["sensitive.keys"].each {
            if (clonedParams[it]) {
                clonedParams[it] = "****"
            }
        }

        clonedParams
    }
}
