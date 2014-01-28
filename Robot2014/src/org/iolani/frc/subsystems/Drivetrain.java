/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;
import org.iolani.frc.commands.OperateTankDrive;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;

/**
 *
 * @author iobotics
 */
public class Drivetrain extends Subsystem {
    private Victor _lVictor;
    private Victor _rVictor;
    
    private Solenoid _solenoid;
    
    private RobotDrive _drive;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void init() {
        _lVictor = new Victor(RobotMap.driveLeftPWM);
        _rVictor = new Victor(RobotMap.driveRightPWM);
        
        _solenoid = new Solenoid(RobotMap.driveSolenoid);
        
        _drive = new RobotDrive(_lVictor, _rVictor);
        _drive.setSafetyEnabled(false);
    }        
    
    public void setTank(double left, double right) {
        _drive.tankDrive(left, right);
    }
    
    public void setMecanum(double mag, double dir, double rot) {
        _drive.mecanumDrive_Polar(mag, dir, rot);
    }
    
    public void setPneumaticDriveSwitch(boolean mecanum) {
        _solenoid.set(mecanum);
    }
    
    public boolean isDriveMecanum() {
        return _solenoid.get();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new OperateTankDrive());
    }
}
