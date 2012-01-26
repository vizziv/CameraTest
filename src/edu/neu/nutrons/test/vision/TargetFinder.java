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
    private int hueLow = 42;
    private int hueHigh = 128;
    private int satLow = 115;
    private int satHigh = 255;
    private int lumLow = 196;
    private int lumHigh = 255;
    private double idealAreaPercent = 80.0/432.0; // (tape area)/(rectangle area)
    private double idealRatio = 4.0/3.0;
    private double minScore = .5;
    private int minArea = 500;

    AxisCamera cam;
    private ColorImage colImage;
    private BinaryImage binImage;
    private Target bestTarget = Target.InvalidTarget;
    private int imageHeight;

    public TargetFinder() {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        imageHeight = cam.getResolution().height;
    }

    private void setConstants() {
        SmartDashboard.putInt("Camera: hue lower bound", hueLow);
        SmartDashboard.putInt("Camera: hue upper bound", hueHigh);
        SmartDashboard.putInt("Camera: saturation lower bound", satLow);
        SmartDashboard.putInt("Camera: saturation upper bound", satHigh);
        SmartDashboard.putInt("Camera: lightness lower bound", lumLow);
        SmartDashboard.putInt("Camera: lightness upper bound", lumHigh);
        hueLow = SmartDashboard.getInt("Camera: set hue lower bound", hueLow);
        hueHigh = SmartDashboard.getInt("Camera: set hue upper bound", hueHigh);
        satLow = SmartDashboard.getInt("Camera: set saturation lower bound", satLow);
        satHigh = SmartDashboard.getInt("Camera: set saturation upper bound", satHigh);
        lumLow = SmartDashboard.getInt("Camera: set lightness lower bound", lumLow);
        lumHigh = SmartDashboard.getInt("Camera: set lightness upper nound", lumHigh);
    }

    private double particleScore(double area, double convexArea,
                                 double bboxWidth, double bboxHeight) {
        double areaPercent = area / convexArea;
        double ratio = bboxWidth / bboxHeight;
        return Utils.falloff(areaPercent, idealAreaPercent) *
                Utils.falloff(ratio, idealRatio);
    }

    public boolean processImage() {
        setConstants();
        boolean success = cam.freshImage();
        if(success) {
            try {
                colImage = cam.getImage();
                binImage = colImage.thresholdHSV(hueLow, hueHigh,
                                                 satLow, satHigh,
                                                 lumLow, lumHigh);
                Pointer im = binImage.image;
                int numParticles = NIVision.countParticles(im);
                SmartDashboard.putInt("Number of particles", numParticles);
                // Loop through every particle and calculate a score for each.
                // Partciles that have good enough scores are targets.
                // Keep track of the highest target; that's the one we want.
                double best = 0;
                bestTarget = Target.InvalidTarget;
                for(int i = 0; i < numParticles; i++) {
                    double area = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_AREA);
                    double bboxWidth = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
                    double bboxHeight = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT);
                    double bboxCornerX = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_LEFT);
                    double bboxCornerY = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_TOP);
                    //double convexArea = NIVision.MeasureParticle(im, i, false,
                    //        NIVision.MeasurementType.IMAQ_MT_CONVEX_HULL_AREA);
                    double convexArea = bboxWidth*bboxHeight;
                    double score = particleScore(area, convexArea, bboxWidth, bboxHeight);
                    if(score >= best) {
                        best = score;
                        bestTarget = new Target(bboxWidth, bboxHeight,
                                                bboxCornerX, bboxCornerY);
                        SmartDashboard.putDouble("Target ratio", bboxWidth/bboxHeight);
                        SmartDashboard.putDouble("Target area percent", area/convexArea);
                        SmartDashboard.putDouble("Target area", area);
                        SmartDashboard.putDouble("Target box area", convexArea);
                        SmartDashboard.putDouble("Target score", score);
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
        return success;
    }

    public Target getTarget() {
        return bestTarget;
    }
}
