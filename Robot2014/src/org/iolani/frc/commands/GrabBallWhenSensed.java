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
    private static final int sOPEN    = 1;
    private static final int sWAITING = 2;
    private static final int sCLOSE   = 3;
    private static final int sGRABBED = 4;
    private int _state;
    
    public GrabBallWhenSensed() {
        // Use requires() here to declare subsystem dependencies
        requires(ballGrabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // Grabbed | Ball | Actions
        //    N       N      Wait
        //    N       Y      Close, Grabbed
        //    Y       N      Open, Wait
        //    Y       Y      Grabbed
        if(ballGrabber.isGrabbed()) { 
            _state = ballGrabber.isBallSensed() ? sGRABBED : sOPEN;
        } else {
            _state = ballGrabber.isBallSensed() ? sCLOSE : sWAITING;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //System.out.println("GrabCommand: " + _state + ", " + ballGrabber.isGrabbed() + ", " + ballGrabber.isBallSensed());
        switch(_state) {
            case sOPEN:
                ballGrabber.setGrabbed(false);
                _state = sWAITING;
                break;
            case sWAITING:
                // if we see a ball, close the grabber //
                if(ballGrabber.isBallSensed()) {
                    // special handling: get a head start on closing //
                    ballGrabber.setGrabbed(true);
                    _state = sCLOSE;
                    break;
                }
                break;
            case sCLOSE:
                ballGrabber.setGrabbed(true);
                // we might want a delay here //
                _state = sGRABBED;
                break;
            case sGRABBED:
                // do nothing, just sit here //
                break;
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
