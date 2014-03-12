/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.ResetCatapult;

/**
 * Combined Catapult and Winch mechanism.
 * @author iobotics
 */
public class Catapult extends Subsystem {
    
    private AnalogChannel _positionSensor;
    private DigitalInput  _limitSwitch;
    private Solenoid      _prongValve;
    private Victor        _winchVictor;
    private Solenoid      _winchValve;
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _positionSensor = new AnalogChannel(RobotMap.catapultSensorADC);
        _limitSwitch    = new DigitalInput(RobotMap.catapultSwitchDIO);
        _prongValve     = new Solenoid(RobotMap.catapultProngValve);
        _winchVictor    = new Victor(RobotMap.catapultWinchPWM);
        _winchValve     = new Solenoid(RobotMap.catapultWinchValve);
    }
    
    /**
     * @return the catapult position in degrees, retracted is 0.0 degrees
     */
    public double getPositionDegrees() {
        return _positionSensor.getVoltage() * 360.0 / 5.0;
    }
    
    /**
     * Is the catapult fully retracted as indicated by the limit switch being 
     * tripped.
     * @return true if the catapult is retracted.
     */
    public boolean isRetracted() {
        return _limitSwitch.get();
    }
    
    /**
     * Set whether lob mode (prongs down) is enabled.
     * @param lob - is lob mode enabled
     * @return previous lob mode state
     */
    public boolean setLob(boolean lob) {
        boolean old = isLob();
        _prongValve.set(!lob);
        return old;
    }
    
    /**
     * Is lob mode enabled.
     * @return true if lob mode is enabled
     */
    public boolean isLob() {
        return !_prongValve.get();
    }
    
    /**
     * Manually set the power to the winch. The winch only turns in the retracting
     * direction, which corresponds to positive power.
     * @param power - power to the winch, must be positive
     * @return previous power setting
     */
    public double setWinchPower(double power) {
        if(power < 0) throw new IllegalArgumentException("Power must not be negative:" + power);
        double old = this.getWinchPower();
        _winchVictor.set(power);
        return old;
    }
    
    /**
     * Get the current winch power setting.
     * @return the current power setting
     */
    public double getWinchPower() {
        return _winchVictor.get();
    }
    
    /**
     * Set the state of the winch latch.
     * @param latched - true to latch the winch
     * @return the previous winch latch state
     */
    public boolean setWinchLatched(boolean latched) {
        boolean old = this.isWinchLatched();
        _winchValve.set(latched);
        return old;
    }
    
    /**
     * Is the winch latched.
     * @return true if the winch is latched
     */
    public boolean isWinchLatched() {
        return _winchValve.get();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ResetCatapult());
    }
}
