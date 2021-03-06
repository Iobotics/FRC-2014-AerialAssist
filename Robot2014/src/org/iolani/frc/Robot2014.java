/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.iolani.frc;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.iolani.frc.commands.CommandBase;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot2014 extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("Robot starting...");
        // instantiate the command used for the autonomous period
        //autonomousCommand = new ExampleCommand();

        // Initialize all subsystems
        CommandBase.init();
    }

    public void autonomousInit() {
        System.out.println("Autonomous mode active");
        // schedule the autonomous command (example)
        //autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        System.out.println("Operator mode active");
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //System.out.println("Catapult encoder: " + CommandBase.catapult.getEncoder() + ", Switch: " + CommandBase.catapult.isRetracted());
        //System.out.println("Rstick's getThrottle() is: " + CommandBase.oi.getRightStick().getThrottle() + " and getTwist() is : " + CommandBase.oi.getRightStick().getTwist());
        CommandBase.catapult.debug();
    }
    
    /**
     * Called when disabled most begins.
     */
    public void disabledInit() {
        System.out.println("Disabled mode active");
    }
    
    /**
     * This function is called periodically during disabled mode
     */
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }
    
    public void testInit() {
        System.out.println("Test mode active");
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
