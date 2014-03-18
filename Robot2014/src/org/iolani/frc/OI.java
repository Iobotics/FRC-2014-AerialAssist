
package org.iolani.frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.iolani.frc.commands.AdjustBall;
import org.iolani.frc.commands.CatchBall;
import org.iolani.frc.commands.DefendRobot;
import org.iolani.frc.commands.IntakeAndGrabBall;
import org.iolani.frc.commands.LaunchBall;
import org.iolani.frc.commands.OperateTractionDrive;
import org.iolani.frc.commands.PassBall;
import org.iolani.frc.commands.SetBallGrabber;
import org.iolani.frc.commands.EnableLobShot;
import org.iolani.frc.util.PowerScaler;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
  
    // primary user interface buttons //
    private final Button _tractionButton = new JoystickButton(_lStick, 1);
    private final Button _catchButton = new JoystickButton(_lStick, 2);
    private final Button _passButton = new JoystickButton(_lStick, 3);
    private final Button _cancelGrabButton = new JoystickButton(_lStick, 5);
    private final Button _lockTractionButton = new JoystickButton(_lStick, 7);
    
    private final Button _shootButton = new JoystickButton(_rStick, 1);
    private final Button _intakeButton = new JoystickButton(_rStick, 3);
    private final Button _lobButton = new JoystickButton(_rStick, 4);
    private final Button _ballAdjustButton = new JoystickButton(_rStick, 5);
    private final Button _defenseButton = new JoystickButton(_rStick, 10);
    

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
        _passButton.whileHeld(new PassBall());
        _catchButton.whenPressed(new CatchBall());
        _cancelGrabButton.whileHeld(new SetBallGrabber(false));
        _ballAdjustButton.whenPressed(new AdjustBall());
        _defenseButton.whenPressed(new DefendRobot());
        // ball shooting
        _shootButton.whenPressed(new LaunchBall());
        _lobButton.whileHeld(new EnableLobShot());
    }
    
    private PowerScaler _driveScaler;
    
    public PowerScaler getDriveScaler() {
        return _driveScaler;
    }
    
    /**
     * 
     * @return from 0.0 to 1.0
     */
    public double getVariableIntakePower()  {
        return (1 - _lStick.getTwist()) / 2;
    }
}

