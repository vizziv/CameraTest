package edu.neu.nutrons.test.vision;

import com.sun.cldc.jna.Pointer;
import edu.neu.nutrons.test.Utils;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Identifies rectangular targets on backboards using Axis camera. Processing
 * images and getting target information are in separate methods to allow for
 * the processing to occur in its own thread if desired.
 *
 * @author Ziv
 */
public class TargetFinder {

    // TODO: Find out what coordinate system camera uses.
    // Right now assuming (0,0) (1,0) (2,0)
    //                    (0,1) (1,1) (2,1) etc.

    // TODO: Tune these constants.
    private int hueLow = 0;
    private int hueHigh = 0;
    private int satLow = 0;
    private int satHigh = 0;
    private int lumLow = 0;
    private int lumHigh = 0;
    private double idealAreaPercent = 80.0/432.0; // (tape area)/(rectangle area)
    private double idealRatio = 4.0/3.0;
    private double minScore = .5;
    private int minArea = 250;

    AxisCamera cam = AxisCamera.getInstance();
    private ColorImage colImage;
    private BinaryImage binImage;
    private Target bestTarget = Target.InvalidTarget;
    private int imageHeight;

    public TargetFinder() {
        imageHeight = cam.getResolution().height;
    }

    private void setConstants() {
        // TODO: Check that these methods actually work.
        // (It wasn't very clear how to set them up from the docs.)
        hueLow = SmartDashboard.getInt("Camera: hue lower bound", 0);
        hueHigh = SmartDashboard.getInt("Camera: hue upper bound", 0);
        hueLow = SmartDashboard.getInt("Camera: saturation lower bound", 0);
        hueHigh = SmartDashboard.getInt("Camera: saturation upper bound", 0);
        hueLow = SmartDashboard.getInt("Camera: lightness lower bound", 0);
        hueHigh = SmartDashboard.getInt("Camera: lightness upper nound", 0);
    }

    private double particleScore(double area, double convexArea,
                                 double bboxWidth, double bboxHeight) {
        double areaPercent = area / convexArea;
        double ratio = bboxWidth / bboxHeight;
        return Utils.absFalloff(areaPercent, idealAreaPercent) *
                Utils.absFalloff(ratio, idealRatio);
    }

    public void processImage() {
        setConstants();
        if(cam.freshImage()) {
            try {
                colImage = cam.getImage();
                binImage = colImage.thresholdHSL(hueLow, hueHigh,
                                                 satLow, satHigh,
                                                 lumLow, lumHigh);
                Pointer im = binImage.image;
                int numParticles = NIVision.countParticles(im);
                // Loop through every particle and calculate a score for each.
                // Partciles that have good enough scores are targets.
                // Keep track of the highest target; that's the one we want.
                double minY = imageHeight; // high in air = low pixel #
                bestTarget = Target.InvalidTarget;
                for(int i = 0; i < numParticles; i++) {
                    double area = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_AREA);
                    double convexArea = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_CONVEX_HULL_AREA);
                    double bboxWidth = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
                    double bboxHeight = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT);
                    double bboxCornerX = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_LEFT);
                    double bboxCornerY = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_TOP);
                    double score = particleScore(area, convexArea, bboxHeight, bboxWidth);
                    // Tiny particles that happen to have a good score don't count.
                    if(score > minScore && convexArea > minArea) {
                        if(bboxCornerY + bboxHeight/2 <= minY) {
                            bestTarget = new Target(bboxWidth, bboxHeight,
                                                    bboxCornerX, bboxCornerY);
                        }
                    }
                }
                // Important! Normally you don't have to do this in Java, but
                // because the underlying software is C++ we need to free the
                // memory used by the images.
                colImage.free();
                binImage.free();
            }
            catch(AxisCameraException ex) {
                ex.printStackTrace();
            }
            catch(NIVisionException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Target getTarget() {
        return bestTarget;
    }
}
