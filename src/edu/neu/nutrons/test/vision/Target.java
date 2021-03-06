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
    public final double rawBboxCornerX;
    public final double rawBboxCornerY;
    public final double rawBboxWidth;
    public final double rawBboxHeight;
    // Calculated values.
    public final double centerX;
    public final double centerY;
    public final double ratio;
    public final double rectitude; // It's a pun!
    // To check whether a target is null, we check whether index >= 0.
    public static final Target NullTarget = new Target(-1,-1,-1,-1,-1,-1);

    public Target(int index, double bboxCornerX, double bboxCornerY,
                  double bboxWidth, double bboxHeight, double area) {
        this.index = index;
        rawBboxCornerX = bboxCornerX;
        rawBboxCornerY = bboxCornerY;
        rawBboxWidth = bboxWidth;
        rawBboxHeight = bboxHeight;
        if(index >= 0) {
            centerX = (-TargetFinder.IMAGE_WIDTH/2.0 + bboxCornerX + bboxWidth/2.0) /
                            TargetFinder.IMAGE_WIDTH;
            centerY = (-TargetFinder.IMAGE_HEIGHT/2.0 + bboxCornerY + bboxHeight/2.0) /
                            TargetFinder.IMAGE_HEIGHT;
            ratio = bboxWidth / bboxHeight;
            rectitude = area / (bboxWidth * bboxHeight);
        }
        else {
            centerX = 0;
            centerY = 0;
            ratio = 0;
            rectitude = 0;
        }
    }

    public Target(int index, ParticleAnalysisReport p) {
        this(index, p.boundingRectLeft, p.boundingRectTop, p.boundingRectWidth,
             p.boundingRectHeight, p.particleArea);
    }

    public boolean isNotNull() {
        return index >= 0;
    }
    public boolean isNull() {
        return index < 0;
    }
}
