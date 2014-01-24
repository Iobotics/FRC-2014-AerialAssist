/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author iobotics
 */
public class Drivetrain extends Subsystem {
    private Victor _rVictor;
    private Victor _lVictor;
    
    private RobotDrive _drive;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void init() {
        _rVictor = new Victor(1);
        _lVictor = new Victor(2);
        
        _drive = new RobotDrive(_rVictor.getChannel(), _lVictor.getChannel());
        _drive.setSafetyEnabled(false);
    }        
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
