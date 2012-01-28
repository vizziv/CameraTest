package edu.neu.nutrons.test;

import com.sun.squawk.util.MathUtils;

/**
 * Various helpful functions.
 *
 * @author Ziv
 */
public class Utils {

    public static double normalDist(double x, double mean, double variance) {
        // Values could be near 0, so expm1 is more accurate.
        return MathUtils.expm1(-(x-mean)*(x-mean) / (2*variance)) + 1;
    }
}
