package com.wizpanda.logging

import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest

@CompileStatic
class KernelRemoteAddressResolver {

    private static final List<String> IP_HEADERS = ["X-Real-IP", "Client-IP", "X-Forwarded-For", "Proxy-Client-IP", "rlnclientipaddr"]

    private KernelRemoteAddressResolver() {
    }

    /**
     * Uses {@link javax.servlet.http.HttpServletRequest#getRemoteAddr()} to resolve the remote client or reads some headers.
     *
     * @param request HttpServletRequest
     * @return the IP address of the client or last proxy that sent the request.
     */
    static String getRemoteAddress(HttpServletRequest request) {
        String unknown = "127.0.0.1"
        if (!request) {
            return unknown
        }

        String ipAddress = unknown

        IP_HEADERS.each { header ->
            if (!ipAddress || unknown.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader(header)
            }
        }

        return ipAddress ?: request.getRemoteAddr()
    }
}
