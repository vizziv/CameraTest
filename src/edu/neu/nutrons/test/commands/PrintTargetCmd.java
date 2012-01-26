package edu.neu.nutrons.test.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Processes camera image and prints target's coordinates on dashboard. If there
 * is no target, the values printed are -1.
 *
 * @author Ziv
 */
public class PrintTargetCmd extends CommandBase {

    boolean success = false;

    public PrintTargetCmd() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        success = tracker.processImage();
        if(success) {
            SmartDashboard.putDouble("Target X", tracker.getTarget().centerX);
            SmartDashboard.putDouble("Target Y", tracker.getTarget().centerY);
        }
        return success;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
