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
    private static final int sDELAY   = 2;
    private static final int sWAITING = 3;
    private static final int sCLOSE   = 4;
    private static final int sGRABBED = 5;
    
    private static final double OPEN_DELAY = 0.25;
    
    private final boolean _quitWhenGrabbed;
    private final boolean _async;
    private int _state;
    
    /**
     * Constructor
     * @param quitWhenGrabbed - should the command terminate when a ball is grabbed
     * @param async - use asynchronous notification 
     */
    public GrabBallWhenSensed(boolean quitWhenGrabbed, boolean async) {
        _quitWhenGrabbed = quitWhenGrabbed;
        _async = async;
        // Use requires() here to declare subsystem dependencies
        requires(ballGrabber);
    }

    /**
     * Constructor.
     * @param quitWhenGrabbed - should the command terminate when a ball is grabbed
     */
    public GrabBallWhenSensed(boolean quitWhenGrabbed) {
        this(quitWhenGrabbed, false);
    }
    
    /**
     * Constructor.
     */
    public GrabBallWhenSensed() {
        this(true, false);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        _state = sOPEN;
        
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
                _state = sDELAY;
                break;
            case sDELAY:
                if(this.timeSinceInitialized() > OPEN_DELAY) {
                    _state = sWAITING;
                }
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
        return _quitWhenGrabbed && _state == sGRABBED;
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
