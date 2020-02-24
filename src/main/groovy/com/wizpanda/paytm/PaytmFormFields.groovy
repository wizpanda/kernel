package com.wizpanda.paytm

import grails.compiler.GrailsCompileStatic
import grails.util.Holders

/**
 * Class to hold methods required to bind payTm form data properly
 *
 * @author Viplav Soni
 */
@GrailsCompileStatic
class PaytmFormFields {

    private static final String MERCHANT_ID = Holders.flatConfig.get("paytm.merchantID")
    private static final String WEBSITE = Holders.flatConfig.get("paytm.website")
    private static final String INDUSTRY_TYPE_ID = Holders.flatConfig.get("paytm.industryTypeID")
    private static final String CHANNEL_ID = Holders.flatConfig.get("paytm.channelID")
    static final String TRANSACTION_URL = Holders.flatConfig.get("paytm.transactionURL")

    Map user
    String orderID
    BigDecimal amount
    String callbackURL

    String checksum
    Map hiddenParams = new TreeMap<String, String>()

    PaytmFormFields(Map user, String orderID, BigDecimal amount, String callbackURL) {
        this.user = user
        this.orderID = orderID
        this.amount = amount
        this.callbackURL = callbackURL

        this.populateHiddenParams()
    }

    private void populateHiddenParams() {
        hiddenParams.put("MID", MERCHANT_ID)
        hiddenParams.put("WEBSITE", WEBSITE)
        hiddenParams.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID)
        hiddenParams.put("CHANNEL_ID", CHANNEL_ID)
        hiddenParams.put("ORDER_ID", orderID)
        hiddenParams.put("CUST_ID", user.id)
        hiddenParams.put("MOBILE_NO", user.mobile)
        hiddenParams.put("EMAIL", user.email)
        hiddenParams.put("TXN_AMOUNT", amount.toString())
        // https://stackoverflow.com/questions/29079574/gstringimpl-cannot-be-cast-to-java-lang-string
        hiddenParams.put("CALLBACK_URL", callbackURL)

        checksum = PaytmUtils.generateChecksum(hiddenParams as TreeMap<String, String>)
    }

}
