/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
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
    
    private Solenoid _valve;
    
    private RobotDrive _drive;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void init() {
        _lfVictor = new Victor(RobotMap.driveLeftFrontPWM);
        _lrVictor = new Victor(RobotMap.driveLeftRearPWM);
        _rfVictor = new Victor(RobotMap.driveRightFrontPWM);
        _rrVictor = new Victor(RobotMap.driveRightRearPWM);
        
        _valve = new Solenoid(RobotMap.driveModeValve);
        
        _drive = new RobotDrive(_lfVictor, _lrVictor, _rfVictor, _rrVictor);
        _drive.setSafetyEnabled(false);
        
        this.setMecanumMode(true);
    }        
    
    public void setArcade(double move, double rotate) {
        if(this.isMecanumMode()) {
            this.setMecanumMode(false);
        }
        _drive.arcadeDrive(move, rotate);
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
        if(mecanum) {
            _drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft,  true);
            _drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft,   true);
            _drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
            _drive.setInvertedMotor(RobotDrive.MotorType.kRearRight,  false);
        } else {
            _drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft,  false);
            _drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft,   false);
            _drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
            _drive.setInvertedMotor(RobotDrive.MotorType.kRearRight,  false);
        }
        _valve.set(!mecanum);
    }
    
    public boolean isMecanumMode() {
        return !_valve.get();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new OperateMecanumDrive());
    }
}
