/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.RetractCatapult;

/**
 *
 * @author iobotics
 */
public class Catapult extends Subsystem {
    
    private Solenoid _valve;
    private Encoder _encoder;
    private DigitalInput _switch;
    private Victor _winch;
    private PIDController _pid;
    
    private static final double kP = 1.0;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    
    private static final double ENCODER_TICKS_PER_INCH = Math.PI / 250.0;
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _valve = new Solenoid(RobotMap.catapultLatchValve);
        _encoder = new Encoder(RobotMap.catapultEncoderADIO, RobotMap.catapultEncoderBDIO);
        _encoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
        _encoder.setDistancePerPulse(ENCODER_TICKS_PER_INCH);
        _encoder.setReverseDirection(true);
        _switch = new DigitalInput(RobotMap.catapultSwitchDIO);
        _winch = new Victor(RobotMap.catapultWinchPWM);
        
        _pid = new PIDController(kP, kI, kD, _encoder, _winch);
        _pid.setOutputRange(-0.5, 0.5);
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
        long old = this.getEncoder();
        _encoder.reset();
        return old;
    }
    
    public long getEncoder() { 
        return _encoder.get();
    }
    
    public boolean isRetracted() {
        return _switch.get();
    }
    
    public double setWinchPower(double power) {
        double old = this.getWinchPower();
        _winch.set(-power);
        return old;
    }
    
    public double getWinchPower() {
        return -_winch.get();
    }
    
    public boolean setHeld(boolean value) {
        boolean old = this.isHeld();
        if(value != old) {
            if(!old) {
                // if was off, enable //
                this.resetEncoder();
                _pid.setSetpoint(0.0);
                _pid.enable();
            } else {
                // if on, disable it //
                _pid.disable();
            }
        }
        return old;
    }
    
    public boolean isHeld() {
        return _pid.isEnable();
    }
    
    public void initDefaultCommand() {
        //setDefaultCommand(new DisableCatapult());
        setDefaultCommand(new RetractCatapult());
    }
    
    public void debug() {
        //System.out.println("enc: " + this.getEncoder() + ", " + _pid.isEnable() + ", " + _pid.getError() + ", " + _pid.get());
    }
}
