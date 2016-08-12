package com.wizpanda.utils

class NumberToWord {
    static List specialNames = [ "", " Thousand", " Million", " Billion", " Trillion", " Quadrillion", "Quintillion"];

    static List tensNames = [
            "", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"
    ];

    static List numNames = [ "", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " " +
            "Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen", " Eighteen", " Nineteen"
    ];

    private String convertLessThanOneThousand(int number) {
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


    String numberToWords(double invoiceAmount) {
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
            double n = invoiceAmount % 1000;
            if (n != 0) {
                String s = convertLessThanOneThousand(n);
                current = s + specialNames[place] + current;
            }
            place++;
            invoiceAmount /= 1000;
        }

        return prefix + " " + current
    }
}
