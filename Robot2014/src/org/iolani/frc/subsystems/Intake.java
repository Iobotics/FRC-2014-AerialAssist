/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;

/**
 *
 * @author iobotics
 */
public class Intake extends Subsystem {
    private Victor _lVictor;
    private Victor _rVictor;

    public void init() {
        _lVictor = new Victor(RobotMap.intakeLeftPWM);
        _rVictor = new Victor(RobotMap.intakeRightPWM);
    }
    
    public void setPower(double pwr) {
        _lVictor.set(pwr);
        _rVictor.set(-pwr);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
