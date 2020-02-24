package com.wizpanda.paytm

import com.paytm.pg.merchant.CheckSumServiceHelper
import grails.compiler.GrailsCompileStatic
import grails.util.Holders
import javax.servlet.http.HttpServletRequest

/**
 * Class to hold methods to generate and verify checksum for payTm validation of data tampering
 *
 * @author Viplav Soni
 */
@GrailsCompileStatic
class PaytmUtils {

    private static final String MASTER_KEY = Holders.flatConfig.get("paytm.masterKey")
    private static final List<String> PARAMS_TO_EXCLUDE_IN_CHECKSUM = ["CHECKSUMHASH"]

    static String generateChecksum(TreeMap<String, String> hiddenParams) {
        CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MASTER_KEY, hiddenParams)
    }

    static boolean verifyChecksum(TreeMap<String, String> paytmParams, String paytmChecksum) {
        CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(MASTER_KEY, paytmParams, paytmChecksum)
    }

    static TreeMap getParametersForChecksum(HttpServletRequest request) {
        TreeMap<String, String> paytmParams = new TreeMap<String, String>()

        request.getParameterMap().each { String key, String[] values ->

            if (!PARAMS_TO_EXCLUDE_IN_CHECKSUM.contains(key)) {
                paytmParams.put(key, values[0])
            }
        }

        return paytmParams
    }
}
