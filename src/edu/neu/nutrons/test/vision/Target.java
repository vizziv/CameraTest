package edu.neu.nutrons.test.vision;

/**
 * Container for rectangular target information.
 *
 * @author Ziv
 */
public class Target {

    public final double xDim;
    public final double yDim;
    public final double xCentroid;
    public final double yCentroid;

    public Target(double xDim, double yDim, double xCentroid, double yCentroid) {
        this.xDim = xDim;
        this.yDim = yDim;
        this.xCentroid = xCentroid;
        this.yCentroid = yCentroid;
    }
}
