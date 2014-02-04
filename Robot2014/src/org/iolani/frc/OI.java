
package org.iolani.frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.StartCommand;
import org.iolani.frc.commands.GrabBallWhenSensed;
import org.iolani.frc.commands.SetBallGrabber;
import org.iolani.frc.commands.SetVariableIntakePower;
import org.iolani.frc.util.PowerScaler;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
  
    private final Button _suckButton = new JoystickButton(_rStick, 3);
    private final Button _blowButton = new JoystickButton(_rStick, 4);
    //private final Button _toggleDriveButton = new JoystickButton(_lStick, 3);
    
    private final Button _grabButton      = new JoystickButton(_lStick, 1);
    private final Button _armGrabButton   = new JoystickButton(_lStick, 3);
    private final Button _cancelArmButton = new JoystickButton(_lStick, 4);
            
    public Joystick getLeftStick() {
        return _lStick;
    }
    
    public Joystick getRightStick() {
        return _rStick;
    }
    
    public OI() {
        CommandGroup suckGroup = new CommandGroup();
        suckGroup.addParallel(new SetVariableIntakePower(true));
        suckGroup.addParallel(new StartCommand(new GrabBallWhenSensed()));
        _suckButton.whileHeld(suckGroup);
        
        CommandGroup blowGroup = new CommandGroup();
        blowGroup.addParallel(new SetVariableIntakePower(false));
        blowGroup.addParallel(new StartCommand(new SetBallGrabber(false)));
        _blowButton.whileHeld(blowGroup);
        
        //_toggleDriveButton.whenPressed(new ToggleDrive());
        _grabButton.whileHeld(new SetBallGrabber(true));
        _armGrabButton.whenPressed(new GrabBallWhenSensed());
        _cancelArmButton.whenPressed(new SetBallGrabber(false));
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

