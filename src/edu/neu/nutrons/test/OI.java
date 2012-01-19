package edu.neu.nutrons.test;

import edu.neu.nutrons.test.commands.PrintTargetCmd;
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
    private Button targetBtn = new JoystickButton(pad, 1);

    public OI() {
        targetBtn.whenPressed(new PrintTargetCmd());
    }

    public double lPowerDT() {
        return -pad.getRawAxis(2);
    }

    public double rPowerDT() {
        return -pad.getRawAxis(4);
    }
}
