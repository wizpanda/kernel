package com.wizpanda.paytm

import grails.compiler.GrailsCompileStatic

/**
 * Common class to hold additional data which need to be send to payTm for transaction.
 *
 * @author Viplav Soni
 */
@GrailsCompileStatic
class PaytmAdditionalData {

    String callbackURL
    PaymentChannel channelID
}

enum PaymentChannel {
    WEB,
    WAP
}
