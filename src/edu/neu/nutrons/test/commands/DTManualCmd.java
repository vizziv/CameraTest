package edu.neu.nutrons.test.commands;

/**
 * Drive train is controlled by OI. Normal tank drive.
 *
 * @author Ziv
 */
public class DTManualCmd extends CommandBase {

    public DTManualCmd() {
        requires(dt);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        dt.driveLR(oi.lPowerDT(), -oi.rPowerDT());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        // This never should get called, but it's here just in case.
        dt.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        // This is probably not important even if it is called, but again we
        // won't take an chances with potentially runaway robots.
        dt.stop();
    }
}
