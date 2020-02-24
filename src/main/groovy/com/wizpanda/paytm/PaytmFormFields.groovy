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
    static final String TRANSACTION_URL = Holders.flatConfig.get("paytm.transactionURL")

    PaytmUser user
    PaytmOrder paytmOrder
    PaytmAdditionalData additionalData

    String checksum
    Map<String, String> hiddenParams = new TreeMap<String, String>()

    PaytmFormFields(PaytmUser user, PaytmOrder paytmOrder, PaytmAdditionalData additionalData) {
        this.user = user
        this.paytmOrder = paytmOrder
        this.additionalData = additionalData

        this.populateHiddenParams()
    }

    private void populateHiddenParams() {
        hiddenParams.put("MID", MERCHANT_ID)
        hiddenParams.put("WEBSITE", WEBSITE)
        hiddenParams.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID)
        hiddenParams.put("CHANNEL_ID", additionalData.channelID.toString())
        hiddenParams.put("ORDER_ID", paytmOrder.orderID)
        hiddenParams.put("CUST_ID", user.id)
        hiddenParams.put("MOBILE_NO", user.mobile)
        hiddenParams.put("EMAIL", user.email)
        hiddenParams.put("TXN_AMOUNT", paytmOrder.amount.toString())
        // https://stackoverflow.com/questions/29079574/gstringimpl-cannot-be-cast-to-java-lang-string
        hiddenParams.put("CALLBACK_URL", additionalData.callbackURL.toString())

        checksum = PaytmUtils.generateChecksum(hiddenParams as TreeMap<String, String>)
    }
}
