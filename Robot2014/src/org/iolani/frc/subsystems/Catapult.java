/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetCatapultLob;

/**
 *
 * @author iobotics
 */
public class Catapult extends Subsystem {
    
    private AnalogChannel _positionSensor;
    private DigitalInput  _limitSwitch;
    private Solenoid      _prongValve;
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _positionSensor = new AnalogChannel(RobotMap.catapultSensorADC);
        _limitSwitch    = new DigitalInput(RobotMap.catapultSwitchDIO);
        _prongValve     = new Solenoid(RobotMap.catapultProngValve);
    }
    
    public double getPositionDegrees() {
        return _positionSensor.getVoltage() * 360.0 / 5.0;
    }
    
    public boolean isRetracted() {
        return _limitSwitch.get();
    }
    
    public boolean setLob(boolean lob) {
        boolean old = isLob();
        _prongValve.set(!lob);
        return old;
    }
    
    public boolean isLob() {
        return !_prongValve.get();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new SetCatapultLob(false));
    }
}
