package edu.neu.nutrons.test.commands;

/**
 * Sets the drive train's setpoint to the angle of the camera's servo.
 *
 * @author Ziv
 */
public class ResetGyroToCamServoCmd extends CommandBase {

    public ResetGyroToCamServoCmd() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        dt.gyro.reset();
        dt.setSetpoint(cam.getAngle());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
