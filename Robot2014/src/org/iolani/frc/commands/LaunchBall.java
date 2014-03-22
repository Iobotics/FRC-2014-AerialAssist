/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author jmalins
 */
public class LaunchBall extends CommandBase {
    
    // states //
    private static final int sOPEN     = 1;
    private static final int sOPENWAIT = 2;
    private static final int sLOBWAIT  = 3;
    private static final int sFIRE     = 4;
    private static final int sFIREWAIT = 5;
    private static final int sDONE     = 6;
    
    private static final double OPEN_DELAY = 0.25;
    private static final double LOB_DELAY  = 0.25;
    private static final double FIRE_DELAY = 1.0;
    
    private int    _state;
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
                if((this.timeSinceInitialized() - _timer) < OPEN_DELAY) break;
                if(oi.getLobButton()) {
                    catapult.setLob(true);
                    _state = sLOBWAIT;
                    _timer = this.timeSinceInitialized();
                } else {
                    _state = sFIRE;
                }
                break;
            case sLOBWAIT:
                if((this.timeSinceInitialized() - _timer) < LOB_DELAY) break;
                _state = sFIRE;
                break;
            case sFIRE:
                catapult.setWinchLatched(false);
                _state = sFIREWAIT;
                _timer = this.timeSinceInitialized();
                break;
            case sFIREWAIT:
                if((this.timeSinceInitialized() - _timer) < FIRE_DELAY) break;
                // reset everything //
                catapult.setWinchLatched(true);
                catapult.setLob(false);
                _state = sDONE;
                break;
            case sDONE:
                // won't get here //
                break;
        }
        SmartDashboard.putString("catapult-launch-state", this.getStateName());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // run continuously until interrupted //
        return _state == sDONE;
    }

    // Called once after isFinished returns true
    protected void end() {
        SmartDashboard.putString("catapult-launch-state", "");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        throw new IllegalStateException("Cannot interrupt ball launch");
    }
    
    private String getStateName() {
        switch(_state) {
            case sOPEN:     return "OPEN";
            case sOPENWAIT: return "OPENWAIT";
            case sLOBWAIT:  return "LOBWAIT";
            case sFIRE:     return "FIRE";
            case sFIREWAIT: return "FIREWAIT";
            case sDONE:     return "DONE";
        }
        return "UNKNOWN";
    }
}
