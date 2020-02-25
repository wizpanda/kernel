package com.wizpanda.paytm

import grails.compiler.GrailsCompileStatic

/**
 * Common class to hold data of order which need to be send to payTm for transaction.
 *
 * @author Viplav Soni
 */
@GrailsCompileStatic
class PaytmOrder {

    BigDecimal amount
    String orderID
}
