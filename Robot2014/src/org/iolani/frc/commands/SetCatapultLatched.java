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
public class SetCatapultLatched extends CommandBase {
    
    private final boolean _latched;
    private final double  _delaySec;
    
    /**
     * Constructor. Set the Catapult latched state with a delay.
     * @param latched - the new latched value
     * @param delayMs - delay (in milliseconds) if the value is different than 
     *                  the current value
     */
    public SetCatapultLatched(boolean latched, int delayMs) {
        _latched  = latched;
        _delaySec = delayMs / 1000.0;
        // Use requires() here to declare subsystem dependencies
        requires(catapult);
    }
    
    /**
     * Constructor. Set the Catapult latched state with no delay.
     * @param latched
     */
    public SetCatapultLatched(boolean latched) {
        this(latched, 0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        catapult.setWinchLatched(_latched);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (this.timeSinceInitialized() >= _delaySec);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}