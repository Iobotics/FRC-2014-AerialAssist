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
public class LaunchBallAndRetract extends CommandGroup {
    
    public LaunchBallAndRetract() {
        this.addSequential(new LaunchBall());
        this.addParallel(new SetBallGrabber(true, 500));
        this.addParallel(new RetractCatapult());
    }
}
