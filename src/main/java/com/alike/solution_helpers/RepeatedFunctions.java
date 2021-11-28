package com.alike.solution_helpers;

/**
 * Class contains some useful functions that are repeated multiple times in the project (or have no obvious space).
 * @author alike
 */
public class RepeatedFunctions {

    /**
     * Outputs a boolean describing whether the input number was a power of two or not.
     * @param num The number we are checking.
     * @return boolean true if the number is a power of 2, false if not.
     */
    public static boolean isPowerOfTwo(double num) {
        while (num > 1) {
            num = num / 2;
        }
        return num == 1.00;
    }

}