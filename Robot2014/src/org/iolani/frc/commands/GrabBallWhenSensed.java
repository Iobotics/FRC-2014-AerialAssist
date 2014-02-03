/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 * Open the ball grabber and wait until the sensor detects a ball. When a ball is
 * sensed, close the grabber. This command continues to run until canceled.
 * 
 * @author jmalins
 */
public class GrabBallWhenSensed extends CommandBase {
    // states //
    private static final int sWAITING = 1;
    private static final int sCLOSE   = 2;
    private static final int sGRABBED = 3;
    private int _state;
    
    public GrabBallWhenSensed() {
        // Use requires() here to declare subsystem dependencies
        requires(ballGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // only set the state here, since if the command starts with   //
        // a ball already present, we don't want the mechanism to open //
        // and close rapidly.                                          //
        _state = sWAITING;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        switch(_state) {
            case sWAITING:
                // if we see a ball, close the grabber //
                if(ballGrabber.isBallSensed()) {
                    ballGrabber.setGrabbed(true);
                    _state = sCLOSE;
                    break;
                }
                break;
            case sCLOSE:
                // we might want a delay here //
                _state = sGRABBED;
                break;
            case sGRABBED:
                // do nothing, just sit here //
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // run continuously until interrupted //
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        // do not change the grabber state, since we don't know if the command    //
        // that interrupted us (the only way to make this command end) is opening //
        // or closing the grabber and we don't want to switch it rapidly          //
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
