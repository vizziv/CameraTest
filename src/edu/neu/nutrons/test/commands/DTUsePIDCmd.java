package edu.neu.nutrons.test.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 * Used for debugging drive train turning PID.
 *
 * @author Ziv
 */
public class DTUsePIDCmd extends CommandBase {

    private static final double TOLERANCE = 3;
    private static final double TIME_SETTLE = .5;
    
    private Timer t = new Timer();

    public DTUsePIDCmd() {
        requires(dt);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        t.reset();
        t.start();
        dt.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Math.abs(dt.getSetpoint() - dt.getPosition()) > TOLERANCE){
            t.reset();
        }
        return t.get() > TIME_SETTLE;
    }

    // Called once after isFinished returns true
    protected void end() {
        dt.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
