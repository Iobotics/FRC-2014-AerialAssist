/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author jmalins
 */
public class SetCatapultLob extends CommandBase {
    
    private final boolean _lob;
    
    public SetCatapultLob(boolean lob) {
        _lob = lob;
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(catapult.isLob() != _lob) {
            catapult.setLob(_lob);   
        }
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
