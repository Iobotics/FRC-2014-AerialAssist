/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.iolani.frc.commands.SetTractionDrivePower;

/**
 *
 * @author iobotics
 */
public class AutoDriveBackwards extends CommandGroup {
    
    public AutoDriveBackwards(double time, double delay) {
        if(delay > 0) {
            this.addSequential(new WaitCommand(delay));
        }
        this.addSequential(new SetTractionDrivePower(0.75, 0), time);
    }
    
    public AutoDriveBackwards() {
        this(1, -1);
    }
}
