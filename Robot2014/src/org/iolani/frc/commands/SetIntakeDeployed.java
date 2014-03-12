/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class SetIntakeDeployed extends CommandBase {
    
    private final boolean _position;
    
    public SetIntakeDeployed(boolean down) {
        requires(intake);
        _position = down;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(intake.getDeployed() != _position)   {
            intake.setDeployed(_position);
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
