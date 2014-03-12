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
public class SetCatapultLob extends CommandBase {
    
    private final boolean _lob;
    private final double  _delaySec;
    private boolean _changed;
    
    public SetCatapultLob(boolean lob, double delayMs) {
        _lob = lob;
        _delaySec = delayMs / 1000.0;
    }

    public SetCatapultLob(boolean lob) {
        this(lob, 0.0);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        boolean old = catapult.setLob(_lob);
        _changed = (old != _lob);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !_changed || (this.timeSinceInitialized() >= _delaySec);
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
