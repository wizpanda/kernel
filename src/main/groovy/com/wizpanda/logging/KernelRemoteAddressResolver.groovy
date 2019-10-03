package com.wizpanda.logging

import io.sentry.event.helper.RemoteAddressResolver

import javax.servlet.http.HttpServletRequest

class KernelRemoteAddressResolver implements RemoteAddressResolver {

    private static final List<String> IP_HEADERS = ["X-Real-IP", "Client-IP", "X-Forwarded-For", "Proxy-Client-IP", "rlnclientipaddr"]

    static final KernelRemoteAddressResolver instance = new KernelRemoteAddressResolver()

    private KernelRemoteAddressResolver() {

    }

    /**
     * Uses {@link javax.servlet.http.HttpServletRequest#getRemoteAddr()} to resolve the remote client or reads some headers.
     *
     * @param request HttpServletRequest
     * @return the IP address of the client or last proxy that sent the request.
     */
    String getRemoteAddress(HttpServletRequest request) {
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