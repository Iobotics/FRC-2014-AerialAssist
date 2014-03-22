/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class SetIntakeDeployed extends CommandBase {
    
    private final boolean _position;
    private final double  _delaySec;
    
    public SetIntakeDeployed(boolean value, int delayMs) {
        _position = value;
        _delaySec = delayMs / 1000.0;
        // Use requires() here to declare subsystem dependencies
        requires(intake);
    }
    
    public SetIntakeDeployed(boolean value, boolean sticky) {
        this(value, sticky? -1: 0);
    }
    
    /**
     * Constructor. Set the BallGrabber's grabbed state with no delay.
     * @param value 
     */
    public SetIntakeDeployed(boolean value) {
        this(value, 0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        intake.setDeployed(_position);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(_delaySec < 0) return false;
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
    
    public String getName() {
        return super.getName() + "(" + _position + ", " + _delaySec + ")";
    }
}
