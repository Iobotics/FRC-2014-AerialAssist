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
public class RetractCatapult extends CommandBase {
    
    private static final int sRETRACT = 1;
    private static final int sSTOP    = 2;
    private static final int sHOLD    = 3;
    
    private int _state;
    
    public RetractCatapult() {
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // clear any existing conditions //
        catapult.setHeld(false);
        
        _state = catapult.isRetracted()? sHOLD: sRETRACT;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        switch(_state) {
            case sRETRACT:
                if(catapult.isRetracted()) {
                    _state = sSTOP;
                    break;
                }
                catapult.setLatched(true);
                catapult.setWinchPower(1.0);
                break;
            case sSTOP:
                catapult.setWinchPower(0.0);
                _state = sHOLD;
                // no break! //
            case sHOLD:
                catapult.setHeld(true);
                break;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        catapult.setHeld(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
