package com.wizpanda.utils

import grails.util.Holders

/**
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class KernelUtils {

    static Object getBean(String serviceName) {
        Holders.getApplicationContext()[serviceName]
    }
}