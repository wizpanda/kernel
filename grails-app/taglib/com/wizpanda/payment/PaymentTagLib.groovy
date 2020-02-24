package com.wizpanda.payment

import com.wizpanda.paytm.PaytmFormFields

/**
 * Common library class for payTm payment integration gsp page
 *
 * @author Viplav Soni
 */
class PaymentTagLib {

    static namespace = "kernel"

    def paytmForm = { attrs, body ->

        PaytmFormFields formFields = attrs.formFields

        out << """<form method="post" action="${formFields.TRANSACTION_URL}" name="paytmForm">"""

        formFields.hiddenParams.each { String key, String value ->
            out << """<input type="hidden" name="${key}" value="${value}">"""
        }

        out << """<input type="hidden" name="CHECKSUMHASH" value="${formFields.checksum}">"""

        out << """
                </form>
                <script type='text/javascript'>
                    document.paytmForm.submit();
                </script>
        """
    }
}
