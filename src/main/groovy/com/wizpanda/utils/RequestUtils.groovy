package com.wizpanda.utils

import com.wizpanda.logging.KernelRemoteAddressResolver
import grails.util.Environment
import grails.util.Holders
import org.grails.web.json.JSONObject
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

    private static maskData(Map params) {
        Map clonedParams = params
        Holders.getFlatConfig()["sensitive.keys"].each {
            if (clonedParams[it]) {
                clonedParams[it] = "****"
            }
        }

        clonedParams
    }

    static Map maskSensitiveData(Map params) {
        maskData(params)
    }

    static JSONObject maskSensitiveData(JSONObject params) {
        maskData(KernelUtils.clone(params))
    }

    /**
     * Get the IP address of the current request. If this Grails app is running behind a proxy then try to get the client IP with some
     * predefined header names.
     * @param request
     * @return
     */
    static getIPAddress(HttpServletRequest request = getCurrentRequest()) {
        KernelRemoteAddressResolver.instance.getRemoteAddress(request)
    }

    /**
     * Construct the server URL based on the incoming request.
     * @param request
     * @return Server URL for example, http://example.com
     */
    static String constructServerURL(HttpServletRequest request = getCurrentRequest()) {
        if (!request) {
            request = getCurrentRequest()
        }

        String protocol = request.scheme
        String serverName = request.serverName
        String port = request.serverPort
        String serverURL = "$protocol://$serverName"
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString())

        if (Environment.isDevelopmentMode()) {
            serverURL += ":$port"
        }
        return serverURL
    }

    /**
     * Generate the complete request URL (along with the query string (if any).
     * @param request
     * @return Request URL for example, http://example.com/api/user/update?username=john
     */
    static String getRequestURL(HttpServletRequest request = getCurrentRequest()) {
        if (!request) {
            request = getCurrentRequest()
        }

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString())
        String queryString = request.getQueryString()

        if (queryString == null) {
            return requestURL.toString()
        }

        return requestURL.append('?').append(queryString).toString()
    }
}
