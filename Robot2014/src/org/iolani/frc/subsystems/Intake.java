/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
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
    
    private Solenoid _valve;
    
    public void init() {
        _lVictor = new Victor(RobotMap.intakeLeftPWM);
        _rVictor = new Victor(RobotMap.intakeRightPWM);
        
        _valve = new Solenoid(RobotMap.intakeDeployValve);
    }
    
    /**
     * Set whether the intake is deployed.
     * @param down - true for down, false for up
     */
    public void setDeployed(boolean down) {
        _valve.set(down);
    }
    
    /**
     * Get whether the intake is deployed.
     * @return - the current intake valve position
     */
    public boolean getDeployed() {
        return _valve.get();
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
        this.setDefaultCommand(new SetIntakePower(0.0));
    }
}
