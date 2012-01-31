package edu.neu.nutrons.test.commands;

import com.sun.squawk.util.MathUtils;
import edu.neu.nutrons.test.vision.TargetFinder;

/**
 * Turns robot to face target.
 *
 * @author Ziv
 */
public class TurnToTargetCmd extends CommandBase {

    private static final double TOLERANCE = 5;

    public TurnToTargetCmd() {
        requires(dt);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        dt.spin(tracker.getTarget1().centerX / (double)TargetFinder.IMAGE_WIDTH);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(tracker.getTarget1().centerX) < TOLERANCE;
    }

    // Called once after isFinished returns true
    protected void end() {
        dt.driveLR(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
