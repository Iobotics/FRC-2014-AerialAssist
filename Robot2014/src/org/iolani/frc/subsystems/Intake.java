/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetIntakePower;

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
    
    /**
     * Set the intake power.
     * @param pwr - positive for suck, negative for blow 
     * @return - the old power
     */
    public double setPower(double pwr) {
        double old = this.getPower();
        _lVictor.set(pwr);
        _rVictor.set(-pwr);
        return old;
    }
    
    /**
     * Get the intake power.
     * @return - the current intake power 
     */
    public double getPower() {
        return _lVictor.get();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new SetIntakePower(0.0));
    }
}
