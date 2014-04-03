/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.iolani.frc.commands.CatchBall;
import org.iolani.frc.commands.LaunchBallAndRetract;

/**
 *
 * @author iobotics
 */
public class AutoDropBallDriveAndShoot extends CommandGroup {
    
    public AutoDropBallDriveAndShoot() {
        this.addParallel(new CatchBall(true));
        this.addSequential(new AutoDriveBackwards(2.25, 1));
        this.addSequential(new LaunchBallAndRetract());
    }
}
