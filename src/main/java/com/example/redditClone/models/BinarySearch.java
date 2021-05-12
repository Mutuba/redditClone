package com.example.redditClone.models;

public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            int mid = (high + low) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                low = low + 1;
            } else {
                high = high - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {

        int sampleArray[] = {10, 12, 34, 56, 67, 78, 79, 85, 89, 90};
        int result = binarySearch(sampleArray, 90);
        System.out.println("The target was found: " + result);

    }
}
