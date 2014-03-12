/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Reset the Catapult to the ready state by retracting it and disabling lob. 
 * @author jmalins
 */
public class ResetCatapult extends CommandGroup {
    
    public ResetCatapult() {
        this.addParallel(new SetCatapultLob(false));
        this.addParallel(new RetractCatapult());
    }
}
