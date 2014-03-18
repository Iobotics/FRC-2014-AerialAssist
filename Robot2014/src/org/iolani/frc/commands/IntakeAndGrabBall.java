/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author jmalins
 */
public class IntakeAndGrabBall extends CommandGroup {
    
    /**
     * Run the intake with variable power and arm the Grabber for grabbing.
     * CommandGroup will exit once ball has been grabbed.
     */
    public IntakeAndGrabBall() {
        this.addParallel(new GrabBallWhenSensed(false)); // keep grab running
        this.addSequential(new SetIntakeDeployed(true));
        this.addSequential(new SetVariableIntakePower());
    }
}
