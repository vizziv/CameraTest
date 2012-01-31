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
                dash.addInt((int)CommandBase.tracker.getTarget1().rawBboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget1().rawBboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget1().rawBboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget1().rawBboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target2 tracking
                dash.addInt((int)CommandBase.tracker.getTarget2().rawBboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget2().rawBboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget2().rawBboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget2().rawBboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target3 tracking
                dash.addInt((int)CommandBase.tracker.getTarget3().rawBboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget3().rawBboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget3().rawBboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget3().rawBboxHeight);
            dash.finalizeCluster();
            dash.addCluster(); // target4 tracking
                dash.addInt((int)CommandBase.tracker.getTarget4().rawBboxCornerX);
                dash.addInt((int)CommandBase.tracker.getTarget4().rawBboxCornerY);
                dash.addInt((int)CommandBase.tracker.getTarget4().rawBboxWidth);
                dash.addInt((int)CommandBase.tracker.getTarget4().rawBboxHeight);
            dash.finalizeCluster();
        dash.finalizeCluster();
        dash.commit();
    }
}
