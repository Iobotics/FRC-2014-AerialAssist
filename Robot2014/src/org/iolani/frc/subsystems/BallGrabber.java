/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetBallGrabber;

/**
 *
 * @author iobotics
 */
public class BallGrabber extends Subsystem {
    private DoubleSolenoid _valve;
    private DigitalInput  _sensor;
    
    public void init() {
        _valve  = new DoubleSolenoid(RobotMap.ballGrabberValve1, RobotMap.ballGrabberValve2);
        _sensor = new DigitalInput(RobotMap.ballSensorDIO);
    }

    public boolean isGrabbed() {
        return _valve.get() == DoubleSolenoid.Value.kForward;
    }
    
    public void setGrabbed(boolean grabbed) {
        _valve.set(grabbed ? DoubleSolenoid.Value.kForward: DoubleSolenoid.Value.kReverse);
    }
    
    public boolean isBallSensed() {
        // sensor is active low //
        return !_sensor.get();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new SetBallGrabber(false));
    }
}
