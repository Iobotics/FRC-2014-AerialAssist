/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
/**
 *
 * @author Aidan
 */
public class TestAutonomous extends CommandGroup {
    
    //Drive forward for 1.5 seconds and shoot.
    
    public TestAutonomous() {
        this.addSequential(new AutonomousDrive(0, 0, 1500));
        this.addSequential(new LaunchBall());
    }
}
