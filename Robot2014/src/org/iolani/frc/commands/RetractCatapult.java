/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Retract the catapult using PID control.
 * @author jmalins
 */
public class RetractCatapult extends CommandBase {
    // states //
    private static final int sSTART      = 1;
    private static final int sINTAKEWAIT = 2;
    private static final int sRETRACT    = 3;
    private static final int sRETRACTING = 4;
    private static final int sHOLD       = 5;
    
    private static final double RETRACT_POSITION_DEGREES = 0.0;
    private static final double INTAKE_DELAY = 0.25;
    
    private int    _state;
    private double _timer;
    
    public RetractCatapult() {
        this.setInterruptible(false);
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
        requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        _state = !catapult.isRetracted()? sSTART: sHOLD;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        switch(_state) {
            case sSTART:
                intake.setDeployed(true);
                _state = sINTAKEWAIT;
                _timer = this.timeSinceInitialized();
                break;
            case sINTAKEWAIT:
                if((this.timeSinceInitialized() - _timer) < INTAKE_DELAY) break;
                _state = sRETRACT;
                break;
            case sRETRACT:
                // enable PID //
                catapult.setPositionSetpoint(RETRACT_POSITION_DEGREES);
                catapult.setWinchLatched(true);
                _state = sRETRACTING;
                break;
            case sRETRACTING:
                //if(catapult.isRetracted() || catapult.isPositionOnTarget()) {
                if(catapult.isRetracted()) {
                    catapult.setWinchPower(0.0); // cancel PID control, stop winch //
                    _state = sHOLD;
                }
                break;
            case sHOLD:
                break;
        }
        SmartDashboard.putString("catapult-retract-state", this.getStateName());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _state == sHOLD;
    }

    // Called once after isFinished returns true
    protected void end() {
        SmartDashboard.putString("catapult-retract-state", "");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        catapult.setWinchPower(0.0); // cancel PID control, stop winch //
        this.end();
    }
    
    private String getStateName() {
        switch(_state) {
            case sSTART:      return "START";
            case sINTAKEWAIT: return "INTAKEWAIT";
            case sRETRACT:    return "RETRACT";
            case sRETRACTING: return "RETRACTING";
            case sHOLD:       return "HOLD";
        }
        return "UNKNOWN";
    }
}