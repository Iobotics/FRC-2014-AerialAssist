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
public class SetMecanumDrivePower extends CommandBase {
    
    private final double _mag;
    private final double _dir;
    private final double _rot;
    
    public SetMecanumDrivePower(double mag, double dir, double rot) {
        _mag = mag;
        _dir = dir;
        _rot = rot;
        // Use requires() here to declare subsystem dependencies
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivetrain.setMecanum(_mag, _dir, _rot);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        drivetrain.setMecanum(0.0, 0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
