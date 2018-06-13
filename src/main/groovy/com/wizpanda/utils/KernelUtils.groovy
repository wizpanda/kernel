package com.wizpanda.utils

import grails.util.Holders
import org.grails.web.json.JSONObject

/**
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class KernelUtils {

    static Object getBean(String serviceName) {
        Holders.getApplicationContext()[serviceName]
    }

    static String getAppName() {
        Holders.getGrailsApplication().metadata["info.app.name"].capitalize()
    }

    static JSONObject clone(JSONObject original) {
        new JSONObject(original, original.names().toArray() as String[])
    }
}