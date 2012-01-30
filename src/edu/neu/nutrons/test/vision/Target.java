package edu.neu.nutrons.test.vision;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * Container for rectangular target information.
 *
 * @author Ziv
 */
public class Target {

    public final int index;
    // Measured values.
    public final double bboxCornerX;
    public final double bboxCornerY;
    public final double bboxWidth;
    public final double bboxHeight;
    // Calculated values.
    public final double centerX;
    public final double centerY;
    public final double ratio;
    // To check whether a target is null, we check whether bboxCornerX >= 0.
    public static final Target NullTarget = new Target(-1,-1,0,0,0);

    public Target(int index, double bboxCornerX, double bboxCornerY,
                  double bboxWidth, double bboxHeight) {
        this.index = index;
        this.bboxCornerX = bboxCornerX;
        this.bboxCornerY = bboxCornerY;
        this.bboxWidth = bboxWidth;
        this.bboxHeight = bboxHeight;
        centerX = bboxCornerX + bboxWidth/2.0;
        centerY = bboxCornerY + bboxHeight/2.0;
        ratio = bboxWidth / bboxHeight;
    }

    public Target(int index, ParticleAnalysisReport p) {
        this(index, p.boundingRectLeft, p.boundingRectTop,
             p.boundingRectWidth, p.boundingRectHeight);
    }

    public boolean isNotNull() {
        return bboxCornerX >= 0;
    }
}
