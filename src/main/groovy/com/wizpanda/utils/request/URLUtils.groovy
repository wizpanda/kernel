package com.wizpanda.utils.request

import groovy.transform.CompileStatic

import java.nio.charset.StandardCharsets

@CompileStatic
class URLUtils {

    static String encode(String value) {
        value ? URLEncoder.encode(value, StandardCharsets.UTF_8.toString()) : null
    }

    static String decode(String value) {
        value ? URLDecoder.decode(value, StandardCharsets.UTF_8.toString()) : null
    }
}
