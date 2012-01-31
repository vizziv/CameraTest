package edu.neu.nutrons.test.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * DEPRECATED
 * Processes camera image and prints target's coordinates on dashboard. Doesn't
 * finish until a target is found.
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
        SmartDashboard.putDouble("Target X", tracker.getTarget1().centerX);
        SmartDashboard.putDouble("Target Y", tracker.getTarget1().centerY);
        dash.sendData();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return tracker.getTarget1().isNotNull();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
