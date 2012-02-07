package edu.neu.nutrons.test.subsystems;

import edu.neu.nutrons.test.RobotMap;
import edu.neu.nutrons.test.commands.CamSetPosCmd;
import edu.neu.nutrons.test.commands.CommandBase;
import edu.neu.nutrons.test.vision.TargetFinder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Controls servo that turns camera; contains target tracker.
 *
 * @author Ziv
 */
public class Camera extends PIDSubsystem {

    public TargetFinder tracker = new TargetFinder();
    private Servo servo = new Servo(RobotMap.CAM_SERVO);
    private static final double kp = .125;
    private static final double kDrive = .125;

    public Camera() {
        super(kp, 0, 0);
    }

    public void setPos(double pos) {
        // Change range from [-1,1] to [0,1].
        servo.set((pos + 1) / 2.0);
    }

    public double getPos() {
        // Change range from [0,1] to [-1,1].
        return 2*servo.get() - 1;
    }

    protected double returnPIDInput() {
        return -tracker.getTarget1().centerX;
    }

    protected void usePIDOutput(double output) {
        // Change position by PID output.
        double wheel = CommandBase.dt.getWheel();
        setPos(getPos() + output - kDrive*wheel*Math.abs(wheel));
    }

    protected void initDefaultCommand() {
        // Always try to keep target in the middle of camera image.
        setDefaultCommand(new CamSetPosCmd(0));
    }
}
