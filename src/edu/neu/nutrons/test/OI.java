package edu.neu.nutrons.test;

import edu.neu.nutrons.test.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Collection of buttons and joysticks. Useful joystick values are available
 * through public methods; buttons are linked with commands.
 *
 * @author Ziv
 */
public class OI {

    private Joystick pad = new Joystick(RobotMap.PAD);
    private Button camLeft = new JoystickButton(pad, 1);
    private Button camCenter = new JoystickButton(pad, 2);
    private Button camRight = new JoystickButton(pad, 3);
    private Button camPoint = new JoystickButton(pad, 4);
    private Button dtTurnToTargetServo = new JoystickButton(pad, 5);
    private Button dtTurnToTarget = new JoystickButton(pad, 6);
    private Button dtUsePID = new JoystickButton(pad, 7);
    private Button gyroToCam = new JoystickButton(pad, 8);

    public OI() {
        camLeft.whenPressed(new CamSetPosCmd(-1));
        camCenter.whenPressed(new CamSetPosCmd(0));
        camRight.whenPressed(new CamSetPosCmd(1));
        camPoint.whenPressed(new CamPointAtTargetCmd());
        dtTurnToTargetServo.whileHeld(new DTTurnToTargetCmd(true));
        dtTurnToTarget.whileHeld(new DTTurnToTargetCmd(false));
        dtUsePID.whileHeld(new DTUsePIDCmd());
        gyroToCam.whenPressed(new ResetGyroToCamServoCmd());
    }

    public double lPowerDT() {
        return -pad.getRawAxis(2);
    }

    public double rPowerDT() {
        return -pad.getRawAxis(4);
    }
}
