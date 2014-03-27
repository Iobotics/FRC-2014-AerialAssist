/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

/**
 * Autonomous driving in traction mode.
 * @author jmalins
 */
public class SetTractionDrivePower extends CommandBase {
    
    private final double _mag;
    private final double _rot;
    
    public SetTractionDrivePower(double mag, double rot) {
        _mag = mag;
        _rot = rot;
        // Use requires() here to declare subsystem dependencies
        requires(drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivetrain.setArcade(_mag, _rot);
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
        drivetrain.setArcade(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
