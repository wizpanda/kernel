package com.wizpanda.utils

import groovy.transform.CompileStatic

@CompileStatic
class NumberToWordUtils {

    static List<String> specialNames = ["", " Thousand", " Million", " Billion", " Trillion", " Quadrillion", "Quintillion"];

    static List<String> tensNames = [
            "", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"
    ];

    static List<String> numNames = ["", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine",
                                    "Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen",
                                    "Seventeen", " Eighteen", " Nineteen"
    ];

    private static String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20) {
            current = numNames[number % 100];
            number /= 100;
        } else {
            current = numNames[number % 10];
            number /= 10;

            current = tensNames[number % 10] + current;
            number /= 10;
        }

        if (number == 0)
            return current;

        return numNames[number] + " hundred" + current;
    }


    static def numberToWords(Double invoiceAmount) {
        if (invoiceAmount == 0) {
            return "zero";
        }

        String prefix = "";

        if (invoiceAmount < 0) {
            invoiceAmount = -invoiceAmount;
            prefix = "negative";
        }
        String current = "";
        int place = 0;
        while (invoiceAmount > 0) {
            int n = (int) invoiceAmount % 1000;
            if (n != 0) {
                String s = convertLessThanOneThousand(n);
                current = s + specialNames[place] + current;
                println(current);
            }
            place++;
            invoiceAmount /= 1000;
        }
        return prefix + " " + current + "Only"
    }
}
