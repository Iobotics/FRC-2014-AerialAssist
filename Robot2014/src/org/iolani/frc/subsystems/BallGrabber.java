/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

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
    
    public void init() {
        _valve = new DoubleSolenoid(RobotMap.ballGrabberValve1, RobotMap.ballGrabberValve2);
    }

    public boolean getGrabbed() {
        return _valve.get() == DoubleSolenoid.Value.kForward;
    }
    
    public void setGrabbed(boolean grabbed) {
        _valve.set(grabbed ? DoubleSolenoid.Value.kForward: DoubleSolenoid.Value.kReverse);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new SetBallGrabber(false));
    }
}
