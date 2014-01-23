/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author wkd
 */
public class NESStick {
    private final Joystick _stick;
    
    public NESStick(int id) {
        _stick = new Joystick(id);
    }
    
    public Joystick getRawJoystick() {
        return _stick;
    }
    
    public double getX() {
        return _stick.getRawAxis(1);
    }
    
    public double getY() {
        return _stick.getRawAxis(5);
    }
    
    public Button getSelectButton() {
        return new JoystickButton(_stick, 9);
    }
    
    public Button getStartButton() {
        return new JoystickButton(_stick, 10);
    }
    
    public Button getAButton() {
        return new JoystickButton(_stick, 2);
    }
    
    public Button getBButton() {
        return new JoystickButton(_stick, 3);
    }
}
