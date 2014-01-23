/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.util;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author wkd
 */
public class JoystickButtonToggle {

    private final JoystickButton _jbutton;
    private final ToggleButton   _ibutton;
    public JoystickButtonToggle(GenericHID joystick, int buttonNumber) {
        _jbutton = new JoystickButton(joystick, buttonNumber);
        _ibutton = new ToggleButton();
        _ibutton.registerToggle();
    }
    
    public boolean isActive() {
        return _ibutton.get();
    }
    
    public void whenActive(final Command command) {
        _ibutton.whileHeld(command);
    }
    
    private class ToggleButton extends InternalButton {
        public void registerToggle() {
            Scheduler.getInstance().addButton(new Button.ButtonScheduler() {
                boolean pressedLast = _jbutton.get();
                public void execute() {
                    if (_jbutton.get()) {
                        if (!pressedLast) {
                            pressedLast = true;
                            ToggleButton.this.setPressed(!ToggleButton.this.get());
                        }
                    } else {
                        pressedLast = false;
                    }
                }
            });
        }
    }
}
