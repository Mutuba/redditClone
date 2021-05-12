package com.example.redditClone.models;


import java.util.HashMap;
import java.util.Map;

class SubArrayCount
{
    // Function to print sub-array having given sum using Hashing
    public static int findSubarray(int[] A, int sum)
    {
        // create an empty map
        Map<Integer, Integer> map = new HashMap<>();

        // insert (0, -1) pair into the set to handle the case when
        // sub-array with given sum starts from index 0
        map.put(0, -1);

        // maintains sum of elements so far
        int sum_so_far = 0;
        int count = 0;
        // traverse the given array
        for (int i = 0; i < A.length; i++)
        {
            // update sum_so_far
            sum_so_far += A[i];

            // if (sum_so_far - sum) is seen before, we have found
            // the sub-array with sum 'sum'
            if (map.containsKey(sum_so_far - sum))
            {
                count++;
            }

            // insert current sum with index into the map
            map.put(sum_so_far, i);
        }
        return count;
    }

    public static void main(String[] args)
    {
        // array of integers
        int[] A = { 0, 5, -7, 1, -4, 7, 6, 1, 4, 1, 10 };
        int sum = 15;

        findSubarray(A, sum);
    }
}