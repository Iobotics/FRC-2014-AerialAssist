/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 * Retract the catapult using PID control.
 * @author jmalins
 */
public class RetractCatapult extends CommandBase {
    
    private static final double RETRACT_POSITION_DEGREES = 0.0;
    
    public RetractCatapult() {
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(!catapult.isRetracted()) {
            // enable PID //
            catapult.setPositionSetpoint(RETRACT_POSITION_DEGREES);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return catapult.isRetracted() || catapult.isPositionOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
        catapult.setWinchPower(0.0); // cancel PID control, stop winch //
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}