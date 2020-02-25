package com.wizpanda.utils

import grails.util.Environment
import grails.util.Holders
import groovy.transform.CompileStatic
import org.grails.web.json.JSONObject

/**
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
@CompileStatic
class KernelUtils {

    /**
     * Get instance of any bean with a given name.
     * @param serviceName
     * @return
     */
    static Object getBean(String serviceName) {
        Holders.getApplicationContext()[serviceName]
    }

    static String getAppName() {
        Holders.getGrailsApplication().metadata["info.app.name"].toString().capitalize()
    }

    static JSONObject clone(JSONObject original) {
        new JSONObject(original, original.names().toArray() as String[])
    }

    static boolean isLocalEnvironment() {
        (Environment.current == Environment.DEVELOPMENT) || (Environment.current == Environment.TEST)
    }
}
