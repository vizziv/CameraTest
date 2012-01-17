package edu.neu.nutrons.test.vision;

import com.sun.cldc.jna.Pointer;
import edu.neu.nutrons.test.Utils;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;

/**
 * Identifies rectangular targets on backboards using Axis camera.
 *
 * @author Ziv
 */
public class TargetFinder {

    private final int HUE_LOW = 0;
    private final int HUE_HIGH = 0;
    private final int SAT_LOW = 0;
    private final int SAT_HIGH = 0;
    private final int LUM_LOW = 0;
    private final int LUM_HIGH = 0;
    private final double TARGET_AREA_PERCENT = 80.0/432.0; // (tape area)/(rectangle area)
    private final double TARGET_RATIO = 4.0/3.0;
    private final double TARGET_MIN_SCORE = .5;
    private final int TARGET_MIN_AREA = 500;

    AxisCamera cam = AxisCamera.getInstance();
    private ColorImage colImage;
    private BinaryImage binImage;
    private Target bestTarget;

    private double particleScore(double area, double convexArea,
                                 double bboxWidth, double bboxHeight) {
        double areaPercent = area / convexArea;
        double ratio = bboxWidth / bboxHeight;
        return Utils.absFalloff(areaPercent, TARGET_AREA_PERCENT) *
                Utils.absFalloff(ratio, TARGET_RATIO);
    }

    public void processImage() {
        if(cam.freshImage()) {
            try {
                colImage = cam.getImage();
                binImage = colImage.thresholdHSL(HUE_LOW, HUE_HIGH,
                                                 SAT_LOW, SAT_HIGH,
                                                 LUM_LOW, LUM_HIGH);
                Pointer im = binImage.image;
                int numParticles = NIVision.countParticles(im);

                // Loop through every particle and calculate a score for each.
                // Partciles that have good enough scores are targets.
                // Keep track of the highest target; that's the one we want.
                for(int i = 0; i < numParticles; i++) {
                    double area = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_AREA);
                    double convexArea = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_CONVEX_HULL_AREA);
                    double bboxHeight = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT);
                    double bboxWidth = NIVision.MeasureParticle(im, i, false,
                            NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
                    double score = particleScore(area, convexArea, bboxHeight, bboxWidth);
                    if(score > TARGET_MIN_SCORE && convexArea > TARGET_MIN_AREA) {
                        // TODO: add to target vector, check for highest target.
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
}
