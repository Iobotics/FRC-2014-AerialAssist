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
    private static final int sINTAKEWAIT = 0;
    private static final int sRETRACT    = 1;
    private static final int sRETRACTING = 2;
    private static final int sHOLD       = 3;
    
    private static final double RETRACT_POSITION_DEGREES = 0.0;
    
    private int     _state;
    private boolean _intakeState;
    
    public RetractCatapult() {
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // initial state logic:                                    //
        //  intake down  retracted  winch latched   initial state  //
        //       Y           -           -           INTAKEWAIT    //
        //       N           N           -           RETRACT       //
        //       N           -           N           RETRACT       //
        //       N           Y           Y           HOLD          //
        
        if(intake.isDeployed()) {
            // HOLD is we are already retracted and latched //
            _state = (catapult.isRetracted() && catapult.isWinchLatched())? sHOLD: sRETRACT;
        } else {
            _state = sINTAKEWAIT;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        switch(_state) {
            case sINTAKEWAIT:
                if(intake.isDeployed()) {
                    _state = sRETRACT;
                }
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
                    _state = sHOLD;
                } else if(!intake.isDeployed()) {
                    _state = sINTAKEWAIT;
                }
                break;
            case sHOLD:
                catapult.setWinchPower(0.0); // cancel PID control, stop winch //
                break;
        }
        SmartDashboard.putString("catapult-retract-state", this.getStateName());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
            case sINTAKEWAIT: return "INTAKEWAIT";
            case sRETRACT:    return "RETRACT";
            case sRETRACTING: return "RETRACTING";
            case sHOLD:       return "HOLD";
        }
        return "UNKNOWN";
    }
}