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
public class EnableLobShot extends CommandBase {
   
    public EnableLobShot() { }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        catapult.setLob(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false; // do not quit //
    }

    // Called once after isFinished returns true
    protected void end() {
        catapult.setLob(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
