package com.wizpanda.payment

import com.wizpanda.paytm.PaytmFormFields
import groovy.transform.CompileStatic

/**
 * Common library class for payTm payment integration gsp page
 *
 * @author Viplav Soni
 */
@CompileStatic
class PaymentTagLib {

    static namespace = "kernel"

    def paytmForm = { Map <String, Object> attrs, body ->

        PaytmFormFields formFields = attrs.formFields as PaytmFormFields

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
