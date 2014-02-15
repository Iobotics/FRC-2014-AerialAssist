/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.OperateMecanumDrive;

/**
 *
 * @author iobotics
 */
public class Drivetrain extends Subsystem {
    private Victor _lfVictor;
    private Victor _lrVictor;
    private Victor _rfVictor;
    private Victor _rrVictor;
    
    private DoubleSolenoid _valve;
    
    private RobotDrive _drive;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void init() {
        _lfVictor = new Victor(RobotMap.driveLeftFrontPWM);
        _lrVictor = new Victor(RobotMap.driveLeftRearPWM);
        _rfVictor = new Victor(RobotMap.driveRightFrontPWM);
        _rrVictor = new Victor(RobotMap.driveRightRearPWM);
        
        _valve = new DoubleSolenoid(RobotMap.driveValve1, RobotMap.driveValve2);
        
        _drive = new RobotDrive(_lfVictor, _lrVictor, _rfVictor, _rrVictor);
        _drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        _drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        _drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
        _drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
        _drive.setSafetyEnabled(false);
    }        
    
    public void setTank(double left, double right) {
        if(this.isMecanumMode()) {
            this.setMecanumMode(false);
        }
        _drive.tankDrive(left, right);
    }
    
    public void setMecanum(double mag, double dir, double rot) {
        if(!this.isMecanumMode()) {
            this.setMecanumMode(true);
        }
        _drive.mecanumDrive_Polar(mag, dir, rot);
    }
    
    public void setMecanumMode(boolean mecanum) {
        _valve.set(mecanum? DoubleSolenoid.Value.kForward: DoubleSolenoid.Value.kReverse);
    }
    
    public boolean isMecanumMode() {
        return _valve.get() == DoubleSolenoid.Value.kForward;
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new OperateMecanumDrive());
    }
}
