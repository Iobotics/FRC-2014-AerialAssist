/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 * @author jmalins
 */
public class ResetIntakeAfterGrab extends CommandGroup {
    
    public ResetIntakeAfterGrab() {
        this.addSequential(new WaitCommand(0.5));
        this.addSequential(new DeployIntakeWhileGrabbed());
    }
}
