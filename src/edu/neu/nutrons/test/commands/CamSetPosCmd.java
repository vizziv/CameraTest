package edu.neu.nutrons.test.commands;

/**
 * Sets camera to certain position. For debugging.
 *
 * @author Ziv
 */
public class CamSetPosCmd extends CommandBase {

    double pos;

    public CamSetPosCmd(double pos) {
        this.pos = pos;
        requires(cam);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        cam.setPos(pos);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
