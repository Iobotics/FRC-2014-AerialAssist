/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class SetVariableIntakePower extends CommandBase {
    
    private boolean _inward;
    
    public SetVariableIntakePower(boolean inward) {
        requires(intake);
        _inward = inward;
    }
    
    public SetVariableIntakePower() {
        this(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(_inward) {
            intake.setPower(oi.getVariableIntakePower());
        } else {
            intake.setPower(-oi.getVariableIntakePower());
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
        intake.setPower(0.0);
    }
}
