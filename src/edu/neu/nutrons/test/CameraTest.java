/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.neu.nutrons.test;

import edu.neu.nutrons.test.commands.CommandBase;
import edu.neu.nutrons.test.commands.DTManualCmd;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CameraTest extends IterativeRobot {

    Command autonomousCommand;

    /**
     * Run when the robot starts up. Initialization code goes here.
     */
    public void robotInit() {
        // Instantiate the command used for the autonomous period.
        autonomousCommand = new DTManualCmd();

        // Initialize all subsystems.
        CommandBase.init();
    }

    public void autonomousInit() {
        // Schedule the autonomous command.
        autonomousCommand.start();
    }

    /**
     * Called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running.
		autonomousCommand.cancel();
    }

    /**
     * Called periodically during operator control.
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Called continuously during operator control.
     */
    public void teleopContinuous() {
        CommandBase.cam.tracker.processImage();
        CommandBase.dash.sendData();
    }
}
