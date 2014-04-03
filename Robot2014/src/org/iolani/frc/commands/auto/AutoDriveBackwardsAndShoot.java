/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.iolani.frc.commands.LaunchBallAndRetract;
import org.iolani.frc.commands.SetBallGrabber;
import org.iolani.frc.commands.SetIntakeDeployed;
import org.iolani.frc.commands.SetTractionDrivePower;

/**
 *
 * @author jmalins
 */
public class AutoDriveBackwardsAndShoot extends CommandGroup {
    
    public AutoDriveBackwardsAndShoot() {
        this.addSequential(new SetTractionDrivePower(0, 0), 1);
        this.addSequential(new SetTractionDrivePower(0.75, 0), 2.25);
        this.addSequential(new WaitCommand(0.5)); // coast to stop //
        this.addParallel(new SetBallGrabber(false));
        this.addParallel(new SetIntakeDeployed(true));
        this.addSequential(new WaitCommand(2.0));
        this.addSequential(new LaunchBallAndRetract());
    }
}
