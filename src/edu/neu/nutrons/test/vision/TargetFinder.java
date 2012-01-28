package edu.neu.nutrons.test.vision;

import com.sun.cldc.jna.Pointer;
import edu.neu.nutrons.test.Utils;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * Identifies rectangular targets on backboards using Axis camera. Processing
 * images and getting target information are in separate methods to allow for
 * the processing to occur in its own thread if desired.
 *
 * @author Ziv
 */
public class TargetFinder {

    // TODO: Tune these constants.
    private int hueLow = 42;
    private int hueHigh = 128;
    private int satLow = 115;
    private int satHigh = 255;
    private int lumLow = 196;
    private int lumHigh = 255;
    private double boxinessIdeal = 80.0/432.0; // (tape area)/(rectangle area)
    private double boxinessTolerance = .5;
    private double ratioIdeal = 4.0/3.0;
    private double ratioTolerance = 1;
    private double inertiaIdeal = .5;
    private double inertiaTolerance = .5;
    private int minArea = 500;
    private double minScore = .9;

    AxisCamera cam;
    private ColorImage colImage;
    private BinaryImage binImage;
    private Target bestTarget = Target.NullTarget;
    private int imageHeight;

    public TargetFinder() {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        imageHeight = cam.getResolution().height;
    }

    private double targetScore(Target target) {
        double boxinessScore = Utils.normalDist(target.boxiness, boxinessIdeal, boxinessTolerance);
        double ratioScore = Utils.normalDist(target.ratio, ratioIdeal, ratioTolerance);
        double inertiaScore = Utils.normalDist(target.inertia, inertiaIdeal, inertiaTolerance);
        return boxinessScore * ratioScore * inertiaScore;
    }

    public boolean processImage() {
        boolean success = cam.freshImage();
        if(success) {
            try {
                colImage = cam.getImage();
                binImage = colImage.thresholdHSV(hueLow, hueHigh,
                                                 satLow, satHigh,
                                                 lumLow, lumHigh);
                Pointer im = binImage.image;
                int numParticles = NIVision.countParticles(im);
                // Loop through every particle and calculate a score for each.
                // Partciles that have good enough scores are targets.
                // Keep track of the highest target; that's the one we want.
                double best = 0;
                bestTarget = Target.NullTarget;
                for(int i = 0; i < numParticles; i++) {
                    double area = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_AREA);
                    if(area > minArea) {
                        double bboxWidth = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
                        double bboxHeight = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT);
                        double bboxCornerX = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_LEFT);
                        double bboxCornerY = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_TOP);
                        double convexArea = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_CONVEX_HULL_AREA);
                        double inertiaX = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_XX);
                        double inertiaY = NIVision.MeasureParticle(im, i, false,
                                NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_YY);
                        Target target = new Target(bboxCornerX, bboxCornerY,
                                                   area, convexArea,
                                                   bboxWidth, bboxHeight,
                                                   inertiaX, inertiaY);
                        double score = targetScore(target);
                        if(score >= best) {
                            best = score;
                            bestTarget = target;
                        }
                    }
                }
                // Important! Normally you don't have to do this in Java, but
                // because the underlying software is C++ we need to free the
                // memory used by the images.
                colImage.free();
                binImage.free();
                // TODO: Find out if we need im.free() here.
            }
            catch(AxisCameraException ex) {
                ex.printStackTrace();
            }
            catch(NIVisionException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }

    public Target getTarget() {
        return bestTarget;
    }
}
