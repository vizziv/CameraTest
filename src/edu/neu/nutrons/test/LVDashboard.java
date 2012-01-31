package edu.neu.nutrons.test;

import edu.neu.nutrons.test.commands.CommandBase;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Sends data back to LabVIEW dashboard on computer. For camera targeting data,
 * sensor feedback, etc.
 *
 * @author Ziv
 */
public class LVDashboard {

    private DriverStation ds = DriverStation.getInstance();
    private Dashboard dash = ds.getDashboardPackerLow();

    public void sendData() {
        dash.addCluster(); // top
            dash.addCluster(); // target1 tracking
                dash.addInt((int)CommandBase.tracker.getTarget1().bboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget1().bboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget1().bboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget1().bboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target2 tracking
                dash.addInt((int)CommandBase.tracker.getTarget2().bboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget2().bboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget2().bboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget2().bboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target3 tracking
                dash.addInt((int)CommandBase.tracker.getTarget3().bboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget3().bboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget3().bboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget3().bboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target4 tracking
                dash.addInt((int)CommandBase.tracker.getTarget4().bboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget4().bboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget4().bboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget4().bboxHeight);
            dash.finalizeCluster();
        dash.finalizeCluster();
        dash.commit();
    }
}
