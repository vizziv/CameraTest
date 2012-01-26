package edu.neu.nutrons.test;

/**
 * Various helpful functions.
 *
 * @author Ziv
 */
public class Utils {

    public static double falloff(double x, double center) {
        return 1/((x/center - 1) * (x*center - 1) + 1);
    }
}
