package com.wizpanda.utils

import org.hashids.Hashids

/**
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class RandomUtils {

    private static final int MINIMUM_RANDOM = 1
    private static final int MAXIMUM_RANDOM = 10000

    /**
     * A helper method to generate random long numbers between defined minimum & maximum number.
     */
    private static long generateRandomNumber() {
        Random rand = new Random()
        return (MINIMUM_RANDOM + rand.nextInt((MAXIMUM_RANDOM - MINIMUM_RANDOM) + 1))
    }

    static String randomPassword() {
        return System.currentTimeMillis().encodeAsBase64().replaceAll("==", "")
    }

    static String randomUniqueCode() {
        Hashids hashids = new Hashids(UUID.randomUUID().toString(), 6)
        return hashids.encode(generateRandomNumber())
    }
}