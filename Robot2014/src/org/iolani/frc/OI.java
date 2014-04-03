
package org.iolani.frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.NetworkButton;
import edu.wpi.first.wpilibj.command.PrintCommand;
import org.iolani.frc.commands.AdjustBall;
import org.iolani.frc.commands.CatchBall;
import org.iolani.frc.commands.CommandBase;
import org.iolani.frc.commands.DefendIntake;
import org.iolani.frc.commands.DefendRobot;
import org.iolani.frc.commands.DeployIntakeWhileGrabbed;
import org.iolani.frc.commands.IntakeAndGrabBall;
import org.iolani.frc.commands.OperateTractionDrive;
import org.iolani.frc.commands.PassBall;
import org.iolani.frc.commands.SetBallGrabber;
import org.iolani.frc.commands.EnableLobShot;
import org.iolani.frc.commands.LaunchBallAndRetract;
import org.iolani.frc.commands.ResetIntakeAfterGrab;
import org.iolani.frc.commands.RetractCatapult;
import org.iolani.frc.commands.SetCatapultLatched;
import org.iolani.frc.commands.SetIntakeDeployed;
import org.iolani.frc.commands.auto.AutoDriveBackwardsAndShoot;
import org.iolani.frc.util.PowerScaler;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
  
    // primary user interface buttons //
    private final Button _tractionButton       = new JoystickButton(_lStick, 1);
    private final Button _catchButton          = new JoystickButton(_lStick, 2);
    private final Button _passButton           = new JoystickButton(_lStick, 3);
    private final Button _backboardCatchButton = new JoystickButton(_lStick, 4);
    private final Button _ballAdjustButton     = new JoystickButton(_lStick, 5);
    private final Button _lockTractionButton   = new JoystickButton(_lStick, 7);
    private final Button _grabberCloseButton   = new JoystickButton(_lStick, 8);
    private final Button _grabberOpenButton    = new JoystickButton(_lStick, 9);
    private final Button _autoTestButton       = new JoystickButton(_lStick, 11);
    
    private final Button _shootButton        = new JoystickButton(_rStick, 1);
    private final Button _intakeButton       = new JoystickButton(_rStick, 3);
    private final Button _lobButton          = new JoystickButton(_rStick, 4);
    private final Button _defendIntakeButton = new JoystickButton(_rStick, 5);
    private final Button _defenseButton      = new JoystickButton(_rStick, 10);
    private final Button _intakeUpButton     = new JoystickButton(_rStick, 8);
    private final Button _intakeDownButton   = new JoystickButton(_rStick, 9);
    private final Button _retractButton      = new JoystickButton(_rStick, 7);
    private final Button _lobTestButton      = new JoystickButton(_rStick, 6);
    private final Button _releaseWinchButton = new JoystickButton(_rStick, 11);
    
    private final NetworkButton _visionButton;
    private final NetworkButton _blobButton;

    private final PowerScaler _mecanumDriveScaler;
    private final PowerScaler _tractionDriveScaler;
    
    public Joystick getLeftStick() {
        return _lStick;
    }
    
    public Joystick getRightStick() {
        return _rStick;
    }
        
    public OI() {
        // traction mode
        _tractionButton.whileHeld(new OperateTractionDrive());
        _lockTractionButton.whenPressed(new OperateTractionDrive());
        // ball handling
        _intakeButton.whileHeld(new IntakeAndGrabBall());
        _intakeButton.whenReleased(new ResetIntakeAfterGrab());
        _passButton.whileHeld(new PassBall());
        
        _catchButton.whenPressed(new CatchBall(true));
        _backboardCatchButton.whenPressed(new CatchBall(false));
        _ballAdjustButton.whileHeld(new AdjustBall());
        _ballAdjustButton.whenReleased(new ResetIntakeAfterGrab());
        _defendIntakeButton.whileHeld(new DefendIntake());
        _defendIntakeButton.whenReleased(new DeployIntakeWhileGrabbed());
        _defenseButton.whenPressed(new DefendRobot());
        _grabberOpenButton.whenPressed(new SetBallGrabber(false, true));
        _grabberCloseButton.whenPressed(new SetBallGrabber(true, true));
        _intakeUpButton.whenPressed(new SetIntakeDeployed(false, true));
        _intakeDownButton.whenPressed(new SetIntakeDeployed(true, true));
        // ball shooting
        _shootButton.whenPressed(new LaunchBallAndRetract());
        _retractButton.whenPressed(new RetractCatapult());
        _lobTestButton.whileHeld(new EnableLobShot());
        _releaseWinchButton.whileHeld(new SetCatapultLatched(false));
        _releaseWinchButton.whenReleased(new SetCatapultLatched(true));
        
        // power scalar from 2013 (mecanum mode) //
        _mecanumDriveScaler = new PowerScaler(new PowerScaler.PowerPoint[] {
                new PowerScaler.PowerPoint(0.0, 0.0),
                new PowerScaler.PowerPoint(0.05, 0.0),
                new PowerScaler.PowerPoint(0.65, 0.5),
                new PowerScaler.PowerPoint(0.80, 1.0)
            });
        // different scaling for traction //
        _tractionDriveScaler = new PowerScaler(new PowerScaler.PowerPoint[] {
                new PowerScaler.PowerPoint(0.0, 0.0),
                new PowerScaler.PowerPoint(0.05, 0.0),
                new PowerScaler.PowerPoint(0.80, 1.0)
            });
        
        // autonomous testing //
        _autoTestButton.whenPressed(new AutoDriveBackwardsAndShoot());
        
        // testing code for vision
        _visionButton  = new NetworkButton(CommandBase.table, "connected");
        _visionButton.whenPressed(new PrintCommand("Hello vision"));
        _blobButton = new NetworkButton(CommandBase.table, "blobDetected");
        _blobButton.whenPressed(new PrintCommand("Houston, we have a blob :)"));
        
    }
    
    public PowerScaler getMecanumDriveScaler() {
        return _mecanumDriveScaler;
    }
    
    public PowerScaler getTractionDriveScaler() {
        return _tractionDriveScaler;
    }
    
    /**
     * 
     * @return from 0.0 to 1.0
     */
    public double getVariableIntakePower()  {
        return (1 - _lStick.getTwist()) / 2;
    }
    
    public boolean getLobButton() {
        return _lobButton.get();
    }
}

