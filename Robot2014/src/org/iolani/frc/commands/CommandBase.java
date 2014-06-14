package org.iolani.frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.iolani.frc.OI;
import org.iolani.frc.subsystems.*;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public final static Drivetrain  drivetrain  = new Drivetrain();
    public final static Intake      intake      = new Intake();
    public final static BallGrabber ballGrabber = new BallGrabber();
    public final static Catapult    catapult    = new Catapult();
    public final static Pneumatics  pneumatics  = new Pneumatics();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be there
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
        
        // initialize the subsystems //
        drivetrain.init();
        intake.init();
        ballGrabber.init();
        catapult.init();
        pneumatics.init();
        
        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(drivetrain);
        SmartDashboard.putData(intake);
        SmartDashboard.putData(ballGrabber);
        SmartDashboard.putData(catapult);
        SmartDashboard.putData(pneumatics);
    }

    public CommandBase(String name, double timeoutSec) {
        super(name, timeoutSec);
    }
    
    public CommandBase(double timeoutSec) {
        super(timeoutSec);
    }
    
    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
