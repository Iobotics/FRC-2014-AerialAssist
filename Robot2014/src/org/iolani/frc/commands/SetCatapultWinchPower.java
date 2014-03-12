/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 * Manually set the winch power on the catapult, this has the effect of disabling
 * PID control of the catapult position.
 * @author jmalins
 */
public class SetCatapultWinchPower extends CommandBase {
    
    private final double _power;
    
    public SetCatapultWinchPower(double power) {
        _power = power;
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        catapult.setWinchPower(_power);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
