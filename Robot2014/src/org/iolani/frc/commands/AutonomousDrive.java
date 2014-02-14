/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 *
 * @author Aidan
 */
public class AutonomousDrive extends CommandBase {
    
    private final double _power;
    private final double _direction;
    private final double _rotation;
    
    public AutonomousDrive(double direction, double rotation, double time) {
        requires(drivetrain);
        _power = 1.0;
        _direction = direction;
        _rotation = rotation;
        this.setTimeout(time);
    }
    
    public AutonomousDrive(double power, double direction, double rotation, double time) {
        requires(drivetrain);
        _power = power;
        _direction = direction;
        _rotation = rotation;
        this.setTimeout(time);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        this.drivetrain.setMecanum(_power, _direction, _rotation); //Not sure what magnitude should be, going with 100% for now.
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        this.drivetrain.setMecanum(0,0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
