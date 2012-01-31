package edu.neu.nutrons.test.vision;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCamera.WhiteBalanceT;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;

/**
 * Identifies rectangular targets on backboards using Axis camera. Processing
 * images and getting target information are in separate methods to allow for
 * the processing to occur in its own thread if desired.
 *
 * @author Ziv
 */
public class TargetFinder {

    // TODO: Tune these constants.
    private final int redLow = 40;
    private final int redHigh = 170;
    private final int greenLow = 10;
    private final int greenHigh = 85;
    private final int blueLow = 0;
    private final int blueHigh = 16;
    private final int bboxWidthMin = 24;
    //private final int bboxHeightMin = 18;
    private final float inertiaXMin = .32f;
    //private final float inertiaYMin = .18f;
    private final double ratioMin = 1;
    private final double ratioMax = 2;
    private final int camBrightness = 10;
    private final int camColor = 100;
    private final WhiteBalanceT camWhiteBalance = WhiteBalanceT.fixedFlour1;

    AxisCamera cam;
    private Target highTarget = Target.NullTarget;
    private Target target1 = Target.NullTarget;
    private Target target2 = Target.NullTarget;
    private Target target3 = Target.NullTarget;
    private Target target4 = Target.NullTarget;
    private final int imageWidth;
    private final int imageHeight;
    private CriteriaCollection boxCriteria;
    private CriteriaCollection inertiaCriteria;

    public TargetFinder() {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeBrightness(camBrightness);
        cam.writeColorLevel(camColor);
        cam.writeWhiteBalance(camWhiteBalance);
        imageWidth = cam.getResolution().width;
        imageHeight = cam.getResolution().height;
        boxCriteria = new CriteriaCollection();
        inertiaCriteria = new CriteriaCollection();
        boxCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH,
                             0, bboxWidthMin, true);
        //boxCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT,
        //                     0, bboxHeightMin, true);
        inertiaCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_XX,
                             0, inertiaXMin, true);
        //inertiaCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_YY,
        //                     0, inertiaYMin, true);
    }

    private void addTarget(Target t) {
        // Fill the first empty target slot.
        if(!target1.isNotNull()) {
            target1 = t;
        }
        else if(!target2.isNotNull()) {
            target2 = t;
        }
        else if(!target3.isNotNull()) {
            target3 = t;
        }
        else if(!target4.isNotNull()) {
            target4 = t;
        }
    }

    public boolean processImage() {
        boolean success = cam.freshImage();
        if(success) {
            try {
                ColorImage im = cam.getImage();
                BinaryImage thresholdIm = im.thresholdRGB(redLow, redHigh,
                                                          greenLow, greenHigh,
                                                          blueLow, blueHigh);
                BinaryImage filteredBoxIm = thresholdIm.particleFilter(boxCriteria);
                BinaryImage filteredInertiaIm = filteredBoxIm.particleFilter(inertiaCriteria);
                ParticleAnalysisReport[] particles = filteredInertiaIm.getOrderedParticleAnalysisReports();
                // Loop through targets, find highest one.
                // Targets aren't found yet.
                highTarget = Target.NullTarget;
                target1 = Target.NullTarget;
                target2 = Target.NullTarget;
                target3 = Target.NullTarget;
                target4 = Target.NullTarget;
                double minY = imageHeight; // Minimum y <-> higher in image.
                System.out.println(particles.length + " particles at " + Timer.getFPGATimestamp());
                for(int i=0; i < particles.length; i++) {
                    Target t = new Target(i, particles[i]);
                    if(t.ratio > ratioMin && t.ratio < ratioMax) {
                        addTarget(t);
                        if(t.centerY <= minY) {
                            highTarget = t;
                        }
                    }
                    System.out.println("Target " + i + ": (" + t.centerX + "," + t.centerY + ")");
                }
                System.out.println("Best target: " + highTarget.index);
                // Free memory from images.
                im.free();
                thresholdIm.free();
                filteredBoxIm.free();
                filteredInertiaIm.free();
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

    public Target getHighestTarget() {
        return highTarget;
    }
    public Target getTarget1() {
        return target1;
    }
    public Target getTarget2() {
        return target2;
    }
    public Target getTarget3() {
        return target3;
    }
    public Target getTarget4() {
        return target4;
    }
}
