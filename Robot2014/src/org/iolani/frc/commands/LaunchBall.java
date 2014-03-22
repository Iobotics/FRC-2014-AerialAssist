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
public class LaunchBall extends CommandBase {
    
    // states //
    private static final int sOPEN     = 1;
    private static final int sOPENWAIT = 2;
    private static final int sFIRE     = 3;
    private static final int sFIREWAIT = 4;
    private static final int sDONE     = 5;
    
    private static final double OPEN_DELAY = 0.25;
    private static final double FIRE_DELAY = 1.0;
    private static final double LOB_SAFE_POSITION = 22.0;
    
    private int _state;
    private double _timer;
        
    public LaunchBall() {
        this.setInterruptible(false);
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
        requires(intake);
        requires(ballGrabber);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        _state = sOPEN;
    }
    
    // Called repeatedly when this Command is scheduled to run
    // Must be synchronized since mutates state and is called from multiple threads
    protected synchronized void execute() {
        //System.out.println("GrabCommand: " + _state + ", " + ballGrabber.isGrabbed() + ", " + ballGrabber.isBallSensed());
        switch(_state) {
            case sOPEN:
                intake.setDeployed(true);
                ballGrabber.setGrabbed(false);
                _state = sOPENWAIT;
                _timer = this.timeSinceInitialized();
                break;
            case sOPENWAIT:
                if((this.timeSinceInitialized() - _timer) > OPEN_DELAY) {
                    _state = sFIRE;
                }
                break;
            case sFIRE:
                catapult.setWinchLatched(false);
                _state = sFIREWAIT;
                _timer = this.timeSinceInitialized();
                break;
            case sFIREWAIT:
                if(oi.getLobButton() && catapult.getPositionDegrees() > LOB_SAFE_POSITION) {
                    catapult.setLob(true);
                }
                if((this.timeSinceInitialized() - _timer) > FIRE_DELAY) {
                    catapult.setWinchLatched(true);
                    catapult.setLob(false);
                    _state = sDONE;
                }
                break;
            case sDONE:
                // won't get here //
                break;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // run continuously until interrupted //
        return _state == sDONE;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        throw new IllegalStateException("Cannot interrupt ball launch");
    }
}
