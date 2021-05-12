package com.example.redditClone.models;

import java.util.Arrays;

public class SubArraySimpleCase {

    public static int[] findSubarray(int[] arr, int sum) {
        int windowSum = 0;
        int high = 0;
        int low = 0;
        for (low = 0; low < arr.length; low++) {
            while (windowSum < sum && high < arr.length) {
                windowSum += arr[high];
                high++;
            }
            if (windowSum == sum) {
//                System.out.printf("The array exists at indexes: [%d - %d]", low, high - 1);
                return new int[]{low, high - 1};
            }

            windowSum -= arr[low];
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int[] A = {2, 6, 0, 9, 7, 3, 1, 4, 1, 10};
        int sum = 15;

        int[] result = findSubarray(A, sum);

        System.out.println("The result for sub array: " + Arrays.toString(result));
    }
}

