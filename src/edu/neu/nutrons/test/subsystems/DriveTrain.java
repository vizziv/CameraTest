package edu.neu.nutrons.test.subsystems;

import edu.neu.nutrons.test.RobotMap;
import edu.neu.nutrons.test.commands.DTManualCmd;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Tank-style drive train.
 *
 * @author Ziv
 */
public class DriveTrain extends PIDSubsystem {

    private static final double kp = .035;
    private static final double kd = .07;

    private Jaguar lMotor = new Jaguar(RobotMap.L_DRIVE_MOTOR);
    private Jaguar rMotor = new Jaguar(RobotMap.R_DRIVE_MOTOR);
    private Encoder lEnc = new Encoder(RobotMap.L_DRIVE_ENC_A, RobotMap.L_DRIVE_ENC_B);
    private Encoder rEnc = new Encoder(RobotMap.R_DRIVE_ENC_A, RobotMap.R_DRIVE_ENC_B);
    public final Gyro gyro = new Gyro(RobotMap.GYRO);
    private double lPower = 0;
    private double rPower = 0;

    public DriveTrain() {
        super(kp, 0, kd);
        disable();
    }

    public void initDefaultCommand() {
        setDefaultCommand(new DTManualCmd());
    }

    public void driveLR(double lPower, double rPower) {
        this.lPower = lPower;
        this.rPower = rPower;
        lMotor.set(-lPower);
        rMotor.set(rPower);
    }

    public void driveCar(double throttle, double wheel) {
        driveLR(throttle + wheel, throttle - wheel);
    }

    public void stop() {
        driveLR(0, 0);
    }

    public double getLPower() {
        return lPower;
    }

    public double getRPower() {
        return rPower;
    }

    public double getThrottle() {
        return (lPower + rPower) / 2.0;
    }

    public double getWheel() {
        if(Math.abs(lPower) < .05 && Math.abs(rPower) < .05) {
            return 0;
        }
        return (lPower - rPower) / 2.0;
    }

    protected double returnPIDInput() {
        return gyro.getAngle();
    }

    protected void usePIDOutput(double output) {
        driveCar(0, output);
    }
}
