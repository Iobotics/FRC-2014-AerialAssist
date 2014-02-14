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
 * @author Aidan
 */
public class TestAutonomous extends CommandGroup {
    
    //IMPORTANT: Currently does not work
    //Drive forward for 0.5 seconds and shoot.
    
    public TestAutonomous() {
        this.addSequential(new AutonomousDrive(0, 0, 500));
        this.addSequential(new WaitCommand(250));
        this.addSequential(new LaunchBall());
    }
}
