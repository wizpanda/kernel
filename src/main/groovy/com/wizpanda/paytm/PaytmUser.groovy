package com.wizpanda.paytm

import grails.compiler.GrailsCompileStatic

/**
 * Class to be used to hold data of user that can be used to send in payTm.
 *
 * @author Viplav Soni
 */
@GrailsCompileStatic
class PaytmUser {

    String id
    String email
    String mobile
}
