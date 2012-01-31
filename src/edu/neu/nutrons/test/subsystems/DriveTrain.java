package edu.neu.nutrons.test.subsystems;

import edu.neu.nutrons.test.RobotMap;
import edu.neu.nutrons.test.commands.DTManualCmd;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Tank-style drive train.
 *
 * @author Ziv
 */
public class DriveTrain extends Subsystem {

    private Jaguar lMotor = new Jaguar(RobotMap.L_DRIVE_MOTOR);
    private Jaguar rMotor = new Jaguar(RobotMap.R_DRIVE_MOTOR);

    public void initDefaultCommand() {
        setDefaultCommand(new DTManualCmd());
    }

    public void driveLR(double lPower, double rPower) {
        lMotor.set(-lPower);
        rMotor.set(rPower);
    }

    public void spin(double wheel) {
        driveLR(wheel, -wheel);
    }

    public void stop() {
        driveLR(0, 0);
    }
}
