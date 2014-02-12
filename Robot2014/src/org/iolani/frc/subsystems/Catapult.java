/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetCatapultLatched;

/**
 *
 * @author iobotics
 */
public class Catapult extends Subsystem {
    
    private Solenoid _valve;
    private Encoder _encoder;
    private DigitalInput _switch;
    /**
     * Initialize the catapult.
     */
    public void init() {
        _valve = new Solenoid(RobotMap.catapultLatchValve);
        _encoder = new Encoder(RobotMap.catapultEncoderADIO, RobotMap.catapultEncoderBDIO);
        _switch = new DigitalInput(RobotMap.catapultSwitchDIO);

        _encoder.start();
    }
    
    public boolean setLatched(boolean latched) {
        boolean old = isLatched();
        _valve.set(!latched);
        return old;
    }
    
    public boolean isLatched() {
        return !_valve.get();
    }
    
    public long resetEncoder() {
        long old = _encoder.get();
        _encoder.reset();
        return old;
    }
    
    public long getEncoder() { 
        return _encoder.get();
    }
    
    public boolean isRetracted() {
        return _switch.get();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new SetCatapultLatched(true));
    }
}
