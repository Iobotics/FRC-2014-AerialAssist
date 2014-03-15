/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import org.iolani.frc.subsystems.BallGrabber;

/**
 * Open the ball grabber and wait until the sensor detects a ball. When a ball is
 * sensed, close the grabber. This command continues to run until canceled.
 * 
 * @author jmalins
 */
public class GrabBallWhenSensed extends CommandBase implements BallGrabber.BallSensedListener {
    // states //
    private static final int sOPEN    = 1;
    private static final int sWAITING = 2;
    private static final int sCLOSE   = 3;
    private static final int sGRABBED = 4;
    
    private final boolean _async;
    private int _state;
    
    /**
     * Constructor
     * @param async - use asynchronous notification 
     */
    public GrabBallWhenSensed(boolean async) {
        _async = async;
        // Use requires() here to declare subsystem dependencies
        requires(ballGrabber);
    }

    /**
     * Constructor.
     */
    public GrabBallWhenSensed() {
        this(false);
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
        
        // add ourselves as a listener if in async mode //
        if(_async) {
            ballGrabber.addBallSensedListener(this);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    // Must be synchronized since mutates state and is called from multiple threads
    protected synchronized void execute() {
        //System.out.println("GrabCommand: " + _state + ", " + ballGrabber.isGrabbed() + ", " + ballGrabber.isBallSensed());
        switch(_state) {
            case sOPEN:
                ballGrabber.setGrabbed(false);
                _state = sWAITING;
                break;
            case sWAITING:
                // if we are in async mode, the ball sensing happens via a //
                // callback on a separate thread, so we don't need to do   //
                // anything here in waiting state.                         //
                if(_async) break;
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
        return _state == sGRABBED;
    }

    // Called once after isFinished returns true
    protected void end() {
        // remove ourselves as a listener if in async mode //
        if(_async) {
            ballGrabber.removeBallSensedListener(this);
        }
        // do not change the grabber state, since we don't know if the command    //
        // that interrupted us (the only way to make this command end) is opening //
        // or closing the grabber and we don't want to switch it rapidly          //
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
    
    /**
     * Callback used in asynchronous mode. Updates the state immediately
     * and runs execute() if a ball is detected.
     */
    public void ballSensed() {
        if(_state == sWAITING) {
            System.out.println("Ball sensed interrupt!");
            _state = sCLOSE;
            this.execute();
        }
    }
}
