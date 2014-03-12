/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;

/**
 *
 * @author jmalins
 */
public class Winch extends Subsystem {
    private Victor   _victor;
    private Solenoid _valve;
    
    public void init() {
        _victor = new Victor(RobotMap.winchPWM);
        _valve  = new Solenoid(RobotMap.winchValve);
    }

    public double setPower(double power) {
        if(power < 0) throw new IllegalArgumentException("Power must not be negative:" + power);
        double old = this.getPower();
        _victor.set(power);
        return old;
    }
    
    public double getPower() {
        return _victor.get();
    }
    
    public boolean setLatched(boolean latched) {
        boolean old = this.isLatched();
        _valve.set(latched);
        return old;
    }
    
    public boolean isLatched() {
        return _valve.get();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //this.setDefaultCommand(new RetractCatapult());
    }
}
