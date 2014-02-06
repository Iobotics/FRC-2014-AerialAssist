/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author iobotics
 */
public class SetBallGrabber extends CommandBase {
    
    private final boolean _value;
    private final double  _delaySec;
    private boolean _changed;
    
    /**
     * Constructor. Set the BallGrabber's grabbed state with a delay.
     * @param value   - the new grabbed value
     * @param delayMs - delay (in milliseconds) if the value is different than 
     *                  the current value
     */
    public SetBallGrabber(boolean value, int delayMs) {
        _value    = value;
        _delaySec = delayMs / 1000.0;
        // Use requires() here to declare subsystem dependencies
        requires(ballGrabber);
    }
    
    /**
     * Constructor. Set the BallGrabber's grabbed state with no delay.
     * @param value 
     */
    public SetBallGrabber(boolean value) {
        this(value, 0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        boolean old = ballGrabber.setGrabbed(_value);
        _changed = (old != _value);
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
