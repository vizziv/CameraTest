package edu.neu.nutrons.test.vision;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

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

    AxisCamera cam = AxisCamera.getInstance();
    private ColorImage colImage;
    private BinaryImage binImage;
    private ParticleAnalysisReport particles[];

    public void handle() {
        if(cam.freshImage()) {
            try {
                colImage = cam.getImage();
                binImage = colImage.thresholdHSL(HUE_LOW, HUE_HIGH,
                                                 SAT_LOW, SAT_HIGH,
                                                 LUM_LOW, LUM_HIGH);
                particles = binImage.getOrderedParticleAnalysisReports();

                // TODO: make image processing do stuff!
                // This line is just a reminder of the functions to use and how
                // to use them. (For example, use binImage.image, which is a
                // Pointer, not an Image. Yeah, confusing.)
                // Regions of interest will probably be important, too.
                double measurement = NIVision.MeasureParticle(binImage.image, 0,
                                  false, NIVision.MeasurementType.IMAQ_MT_AREA);

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
