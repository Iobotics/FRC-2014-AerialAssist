
package org.iolani.frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.iolani.frc.commands.SetVariableIntakePower;
import org.iolani.frc.util.PowerScaler;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private final Joystick _lStick = new Joystick(1);
    private final Joystick _rStick = new Joystick(2);
  
    private final Button _intakeButton = new JoystickButton(_rStick, 3);
            
    public Joystick getLeftStick() {
        return _lStick;
    }
    
    public Joystick getRightStick() {
        return _rStick;
    }
    
    public OI() {
        _intakeButton.whileHeld(new SetVariableIntakePower());
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
        return (_lStick.getTwist() + 1) / 2;
    }
}

