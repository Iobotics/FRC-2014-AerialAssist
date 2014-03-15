/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.ResetCatapult;

/**
 * Combined Catapult and Winch mechanism.
 * @author iobotics
 */
public class Catapult extends Subsystem implements PIDSource {
    
    private AnalogChannel _positionSensor;
    private DigitalInput  _limitSwitch;
    private Solenoid      _prongValve;
    private Victor        _winchVictor;
    private Solenoid      _winchValve;
    private PIDController _pid;
    
    private static final double kP = 0.2;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _positionSensor = new AnalogChannel(RobotMap.catapultSensorADC);
        _limitSwitch    = new DigitalInput(RobotMap.catapultSwitchDIO);
        _prongValve     = new Solenoid(RobotMap.catapultProngValve);
        _winchVictor    = new Victor(RobotMap.catapultWinchPWM);
        _winchValve     = new Solenoid(RobotMap.catapultWinchValve);
        
        _pid = new PIDController(kP, kI, kD, this, _winchVictor);
        _pid.setInputRange(-5.0, 95.0);
        _pid.setOutputRange(0, 1.0); // only send positive values //
        _pid.setPercentTolerance(5.0);
    }
    
    /**
     * @return the catapult position in degrees, retracted is 0.0 degrees
     */
    public double getPositionDegrees() {
        return _positionSensor.getVoltage() * 360.0 / 5.0;
    }
    
    /**
     * Implement PIDSource so we can use the scaled position as our source
     * value for PID control.
     * @return returns the catapult position in degrees
     */
    public double pidGet() {
        return this.getPositionDegrees();
    }
    
    /**
     * Return whether PID control is actively maintaining the catapult position.
     * PID control is automatically enabled if setPositionSetpoint(double) is called.
     * @return true if PID is active
     */
    public boolean isPositionControlled() {
        return _pid.isEnable();
    }
    
    /**
     * Set whether the catapult position is actively maintained with PID control.
     * @param pidEnabled - true to enable PID
     * @return previous state of PID control
     */
    public boolean setPositionControlled(boolean pidEnabled) {
        boolean old = this.isPositionControlled();
        if(pidEnabled) {
            _pid.enable();
        } else {
            _pid.disable();
        }
        return old;
    }
    
    /**
     * Returns the current PID setpoint. PID may or may not be active.
     * @return the current position setpoint in degrees
     */
    public double getPositionSetpoint() {
        return _pid.getSetpoint();
    }
    
    /**
     * Set the desired position setpoint and enable PID control of position.
     * @param degrees - the desired position
     * @return the previous setpoint value
     */
    public double setPositionSetpoint(double degrees) {
        double old = this.getPositionSetpoint();
        this.setPositionControlled(true);
        _pid.setSetpoint(degrees);
        return old;
    }
    
    /**
     * If PID is enabled, returns whether the catapult position is on target
     * (i.e. within the specified tolerance). Returns false if PID is not
     * enabled.
     * @return true if PID is enabled and position is on target, otherwise false
     */
    public boolean isPositionOnTarget() {
        return (this.isPositionControlled())? _pid.onTarget(): false;
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
        _prongValve.set(lob);
        return old;
    }
    
    /**
     * Is lob mode enabled.
     * @return true if lob mode is enabled
     */
    public boolean isLob() {
        return _prongValve.get();
    }
    
    /**
     * Manually set the power to the winch. The winch only turns in the retracting
     * direction, which corresponds to positive power. Calling this method will
     * disable PID position control.
     * @param power - power to the winch, must be positive
     * @return previous power setting
     */
    public double setWinchPower(double power) {
        if(power < 0) throw new IllegalArgumentException("Power must not be negative:" + power);
        this.setPositionControlled(false);
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
