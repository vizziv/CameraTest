package edu.neu.nutrons.test.vision;

/**
 * Container for rectangular target information.
 *
 * @author Ziv
 */
public class Target {

    // Measured values.
    public final double bboxCornerX;
    public final double bboxCornerY;
    public final double area;
    public final double convexArea;
    public final double bboxWidth;
    public final double bboxHeight;
    public final double inertiaX;
    public final double inertiaY;
    // Calculated values.
    public final double centerX;
    public final double centerY;
    public final double boxiness;
    public final double ratio;
    public final double inertia;
    // To check whether a target is null, we check whether bboxCornerX >= 0.
    public static final Target NullTarget = new Target(-1,0,0,0,0,0,0,0);

    public Target(double bboxCornerX, double bboxCornerY,
                  double area, double convexArea,
                  double bboxWidth, double bboxHeight,
                  double inertiaX, double inertiaY) {
        this.bboxCornerX = bboxCornerX;
        this.bboxCornerY = bboxCornerY;
        this.area = area;
        this.convexArea = convexArea;
        this.bboxWidth = bboxWidth;
        this.bboxHeight = bboxHeight;
        this.inertiaX = inertiaX;
        this.inertiaY = inertiaY;
        centerX = bboxCornerX + bboxWidth/2.0;
        centerY = bboxCornerY + bboxHeight/2.0;
        boxiness = (convexArea / bboxWidth) / bboxHeight;
        ratio = bboxWidth / bboxHeight;
        inertia = inertiaX + inertiaY;
    }

    public boolean isNotNull() {
        return bboxCornerX >= 0;
    }
}
