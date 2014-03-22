/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.RetractCatapult;
import org.iolani.frc.util.Utility;

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
    private double _up_volts;
    private double _down_volts;
    
    private static final double kP_DEFAULT = 0.2;
    private static final double kI_DEFAULT = 0.0;
    private static final double kD_DEFAULT = 0.0;
    
    private static final double UP_VOLTS_DEFAULT   = 0.7;
    private static final double DOWN_VOLTS_DEFAULT = 4.3; 
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _positionSensor = new AnalogChannel(RobotMap.catapultSensorADC);
        _limitSwitch    = new DigitalInput(RobotMap.catapultSwitchDIO);
        _prongValve     = new Solenoid(RobotMap.catapultProngValve);
        _winchVictor    = new Victor(RobotMap.catapultWinchPWM);
        _winchValve     = new Solenoid(RobotMap.catapultWinchValve);
        
        Preferences prefs = Preferences.getInstance();
        double kP = prefs.getDouble("catapult.kP", kP_DEFAULT);
        double kI = prefs.getDouble("catapult.kI", kI_DEFAULT);
        double kD = prefs.getDouble("catapult.kD", kD_DEFAULT);
        
        _pid = new PIDController(kP, kI, kD, this, _winchVictor);
        _pid.setInputRange(0, 90);
        _pid.setOutputRange(-1.0, 0); // error will be negative //
        _pid.setPercentTolerance(1.0);
        SmartDashboard.putData("catapult-pid", _pid);
        
        _up_volts   = prefs.getDouble("catapult.ma3_up_volts",   UP_VOLTS_DEFAULT);
        _down_volts = prefs.getDouble("catapult.ma3_down_volts", DOWN_VOLTS_DEFAULT);
    }
    
    /**
     * @return the catapult position in degrees, retracted is 0.0 degrees
     */
    public double getPositionDegrees() {
        double angle = (90 / (_up_volts - _down_volts)) * (this.getPositionSensor() - _down_volts);
        return Utility.window(angle, 0.0, 90.0);
    }
    
    public double getPositionSensor() {
        return _positionSensor.getVoltage();
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
    
    public double getPositionError() {
        return _pid.getError();
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
        _winchValve.set(!latched);
        return old;
    }
    
    /**
     * Is the winch latched.
     * @return true if the winch is latched
     */
    public boolean isWinchLatched() {
        return !_winchValve.get();
    }
    
    public void initDefaultCommand() {
        //setDefaultCommand(new RetractCatapult());
    }
}
