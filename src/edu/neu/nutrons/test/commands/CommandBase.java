package edu.neu.nutrons.test.commands;

import edu.neu.nutrons.test.LVDashboard;
import edu.neu.nutrons.test.OI;
import edu.neu.nutrons.test.subsystems.Camera;
import edu.neu.nutrons.test.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 */
public abstract class CommandBase extends Command {

    // Single static instance of OI, camera cam and each subsystem.
    public static OI oi;
    public static DriveTrain dt = new DriveTrain();
    public static Camera cam = new Camera();;
    public static LVDashboard dash = new LVDashboard();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command each subsystem is running on SmartDashboard.
        SmartDashboard.putData(dt);
        SmartDashboard.putData(cam);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
