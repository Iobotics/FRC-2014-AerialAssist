/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.iolani.frc.commands.LaunchBallAndRetract;
import org.iolani.frc.commands.SetTractionDrivePower;

/**
 *
 * @author jmalins
 */
public class AutoDriveForwardAndShoot extends CommandGroup {
    
    public AutoDriveForwardAndShoot() {
        this.addSequential(new SetTractionDrivePower(0.5, 0.0), 2.0);
        this.addSequential(new WaitCommand(0.5));
        this.addSequential(new LaunchBallAndRetract());
    }
}
