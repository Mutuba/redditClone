package com.example.redditClone.models;

class MaxGap {

    public static int maxGap(int N) {
        // the longest binary string we need to handle in this problem is 31 bits
        final int MAX_STRING_LENGTH = 31;


        // variable used for calculation of our binary string
        int result = N;
        // variable used for storing our binary String
        StringBuilder binaryString = new StringBuilder(MAX_STRING_LENGTH);

        // O(log N) loop for calculating our binary string
        while (result > 0) {
            // calculate each bit in the binary string and prepend it to the string
            // (if we add it to the end, we would have to reverse the string after the loop)
            binaryString.insert(0, result % 2);
            result = result / 2;
        }

        // the biggest binary gap in our binary string
        int maxGapLength = 0;
        int count = 0;
        // loop through our binary string
        // O(log N)
        for (int i = 0; i < binaryString.length(); i++) {
            // if we encounter a 0, then we increment our count variable
            if (binaryString.charAt(i) == '0') {
                count++;
            }
            // if we encounter a 1, then we are finished with our CURRENT gap
            if (binaryString.charAt(i) == '1') {
                // update our maxGapLength if needed
                if (count > maxGapLength) {
                    maxGapLength = count;
                }
                // reset count variable so we can count the length of the next gap
                count = 0;
            }
        }

        return maxGapLength;
    }

    public static void main(String args[]) {
        int num = 9;
        int result = maxGap(num);

        System.out.println("The max gap is: " + result);
    }

}
