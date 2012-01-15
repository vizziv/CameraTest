
package edu.neu.nutrons.test;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Collection of buttons and joysticks. Useful joystick values are available
 * through public methods; buttons are linked with commands.
 *
 * @author Ziv
 */
public class OI {

    private Joystick pad = new Joystick(RobotMap.PAD);

    public double lPowerDT() {
        return -pad.getRawAxis(2);
    }

    public double rPowerDT() {
        return -pad.getRawAxis(4);
    }
}
