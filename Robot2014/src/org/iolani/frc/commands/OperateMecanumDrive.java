/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.GenericHID;
import org.iolani.frc.util.*;

/**
 *
 * @author iobotics
 */
public class OperateMecanumDrive extends CommandBase {
    
    private static final double DEADBAND = 0.05;
    private static final double SLOW_MODE = 0.3;
    
    public OperateMecanumDrive() {
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //drivetrain.startEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double mag = oi.getRightStick().getMagnitude();
        double dir = oi.getRightStick().getDirectionDegrees();
        double rot = oi.getLeftStick().getX();
        PowerScaler scale = oi.getDriveScaler();
        if(scale != null) {
            mag = scale.get(mag);
            rot = scale.get(rot);
        }
        if(Math.abs(mag) < DEADBAND) { mag = 0.0; }
        if(Math.abs(rot) < DEADBAND) { rot = 0.0; }
        
        /*
        if(oi.getSlowModeButton()) {
                mag = SLOW_MODE * mag;
                rot = SLOW_MODE * rot;
        }
        */
        
        //System.out.println(mag + " , " + dir + " , " + rot);
        //System.out.println("LR: " + drivetrain.getLeftRearEncoder() + ", RR: " + drivetrain.getRightRearEncoder());
        //drivetrain.debug();
        
        drivetrain.setMecanum(mag, dir, rot);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}