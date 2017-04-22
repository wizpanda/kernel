package com.wizpanda.utils

import org.hashids.Hashids

/**
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class RandomUtils {

    private static final int MINIMUM_RANDOM = 1
    private static final int MAXIMUM_RANDOM = 100000

    /**
     * A helper method to generate random long numbers between defined minimum & maximum number.
     */
    private static long generateRandomNumber(int min = MINIMUM_RANDOM, int max = MAXIMUM_RANDOM) {
        Random rand = new Random()
        return (min + rand.nextInt((max - min) + 1))
    }

    static String randomPassword() {
        return System.currentTimeMillis().encodeAsBase64().replaceAll("==", "")
    }

    static String randomUniqueCode() {
        return randomUniqueCode(UUID.randomUUID().toString(), 6, generateRandomNumber())
    }

    static String randomUniqueCode(int minLength) {
        return randomUniqueCode(UUID.randomUUID().toString(), minLength, generateRandomNumber())
    }

    static String randomUniqueCode(String salt, int minLength, long... numbers) {
        Hashids hashids = new Hashids(salt, minLength)
        return hashids.encode(numbers)
    }
}