package com.example.redditClone.models;

import java.util.Scanner;

public class Armstrong {

    public static void main(String[] args) {

        // create an object of Scanner
        Scanner input = new Scanner(System.in);

        // take input from the user
        System.out.print("Enter an integer: ");
        int number = input.nextInt();

        int remainder, result = 0;
        int originalNumber = number;

        int numOfDigits = 0;

        for (int num = originalNumber; num > 0; ++numOfDigits) {
            num /= 10;
        }


        while (originalNumber != 0) {
            // modulus division 2116260
            remainder = originalNumber % 10;
            // currentNumber raised to number of digits
            result += Math.pow(remainder, numOfDigits);
            // floor division
            originalNumber /= 10;
        }

        if (result == number)
            System.out.println(number + " is an Armstrong number.");
        else
            System.out.println(number + " is not an Armstrong number.");
    }
}