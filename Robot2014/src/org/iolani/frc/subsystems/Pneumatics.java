/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;

/**
 *
 * @author iobotics
 */
public class Pneumatics extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private final Compressor _compressor;
    
    public Pneumatics() {
        _compressor = new Compressor(RobotMap.pressureSwitchDIO, RobotMap.compressorRelay);
    }
    
    public void init() {
        _compressor.start();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
