package edu.neu.nutrons.test;

/**
 * Various helpful functions.
 *
 * @author Ziv
 */
public class Utils {

    public static double absFalloff(double x, double center) {
        return 1 - Math.abs(x/center - 1);
    }
}
