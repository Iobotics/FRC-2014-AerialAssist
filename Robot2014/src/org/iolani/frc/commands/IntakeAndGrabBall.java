/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.StartCommand;

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
        this.addParallel(new SetVariableIntakePower(true));
        this.addParallel(new StartCommand(new GrabBallWhenSensed()));
    }
}
