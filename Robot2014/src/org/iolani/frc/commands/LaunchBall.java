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
public class LaunchBall extends CommandGroup {
    
    /**
     * Open the BallGrabber and release the catapult.
     */
    public LaunchBall() {
        this.addSequential(new SetBallGrabber(false, 250)); // set ball grabber with delay //
        this.addSequential(new SetCatapultLatched(false));
    }
}
