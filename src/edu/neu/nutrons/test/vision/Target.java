package edu.neu.nutrons.test.vision;

/**
 * Container for rectangular target information.
 *
 * @author Ziv
 */
public class Target {

    public final double dimX;
    public final double dimY;
    public final double centerX;
    public final double centerY;

    public Target(double width, double height, double cornerX, double cornerY) {
        dimX = width;
        dimY = height;
        centerX = cornerX + width/2;
        centerY = cornerY + height/2;
    }

    public boolean isValid() {
        return dimX > 0;
    }

    // To check whether a target is valid, we can check whether dimX > 0.
    public static Target InvalidTarget = new Target(-1,-1,-.5,-.5);
}
