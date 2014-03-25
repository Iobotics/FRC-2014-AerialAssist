/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author koluke
 */
public class CatchBall extends CommandGroup {
    
    public CatchBall(boolean intakeDeployed) {
        this.addParallel(new SetIntakeDeployed(intakeDeployed));
        this.addSequential(new GrabBallWhenSensed());
        if(intakeDeployed) {
            this.addSequential(new ResetIntakeAfterGrab());
        }
    }
     
}
