/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetCatapultLatched;

/**
 *
 * @author iobotics
 */
public class Catapult extends Subsystem {
    
    private Solenoid _valve;
    
    /**
     * Initialize the catapult.
     */
    public void init() {
        _valve = new Solenoid(RobotMap.catapultLatchValve);
    }
    
    public void setLatched(boolean latched) {
        _valve.set(!latched);
    }
    
    public boolean getLatched() {
        return !_valve.get();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new SetCatapultLatched(true));
    }
}
