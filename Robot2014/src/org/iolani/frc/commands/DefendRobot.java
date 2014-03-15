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
public class DefendRobot extends CommandGroup {
    
    public DefendRobot() {
        this.addParallel(new DefendIntake());
        this.addParallel(new SetBallGrabber(false));
    }
}
