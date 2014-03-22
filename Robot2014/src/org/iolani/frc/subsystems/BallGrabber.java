/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptableSensorBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.Enumeration;
import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.SetBallGrabber;
import org.iolani.frc.util.InterruptWatcher;

/**
 * Ball grabber subsystem
 * 
 * @author jmalins
 */
public class BallGrabber extends Subsystem {
    
    private Solenoid      _valve;
    private DigitalInput  _sensor;
    
    // Interrupt watcher used for interrupt mode //
    private InterruptWatcher _watcher = null;
    
    public void init() {
        _valve  = new Solenoid(RobotMap.ballGrabberValve);
        _sensor = new DigitalInput(RobotMap.ballSensorDIO);
    }

    /**
     * Get the grabbed state.
     * @return true if the grabber is grabbed
     */
    public boolean isGrabbed() {
        return !_valve.get();
    }
    
    /**
     * Set whether the grabber is grabbed.
     * @param grabbed new state
     * @return old state
     */
    public boolean setGrabbed(boolean grabbed) {
        boolean old = this.isGrabbed();
        _valve.set(!grabbed);
        return old;
    }
    
    /**
     * Is a ball sensed.
     * @return true if a ball is sensed
     */
    public boolean isBallSensed() {
        // sensor is active low //
        return !_sensor.get();
    }
    
    /**
     * Set the default behavior.
     */
    public void initDefaultCommand() {
        // Default grabber to grabbed to match up with the ball grabber //
        // part of DefendRobot.                                         //
        setDefaultCommand(new SetBallGrabber(true));
    }
    
    /**
     * Add a callback for when a ball is sensed.
     * @param listener - listener to add
     */
    public void addBallSensedListener(BallSensedListener listener) {
        if(findAdapter(listener) == null) {
            if(_watcher == null) {
                // configure interrupts on falling edge of the sensor signal //
                _sensor.requestInterrupts();
                _sensor.setUpSourceEdge(false, true);
                
                // create the watcher //
                _watcher = new InterruptWatcher(_sensor);
            }
            _watcher.addListener(new BallInterruptAdapter(listener));
            if(_watcher.getListenerCount() == 1) {
                _watcher.start();
            }
        }
    }
    
    /**
     * Remove a ball sensed callback.
     * @param listener - listener to remove
     * @return true if the listener was present, false if not
     */
    public boolean removeBallSensedListener(BallSensedListener listener) {
        BallInterruptAdapter adapter = findAdapter(listener);
        if(adapter != null) {
            _watcher.removeListener(adapter);
            if(_watcher.getListenerCount() == 0) {
                _watcher.stop();
            }
            return true;
        }
        return false;
    }
    
    /**
     * Interface used to receive an immediate callback when a ball is sensed. 
     */
    public interface BallSensedListener {
        void ballSensed();
    }
    
    /**
     * Find the adapter corresponding the specified BallSensedListener.
     * @param listener
     * @return 
     */
    private BallInterruptAdapter findAdapter(BallSensedListener listener) {
        if(_watcher == null) return null;
        Enumeration listeners = _watcher.enumerateListeners();
        while(listeners.hasMoreElements()) {
            InterruptWatcher.Listener xlistener = (InterruptWatcher.Listener) listeners.nextElement();
            // don't assume we are the only one adding listeners to the watcher! //
            if(xlistener instanceof BallInterruptAdapter) {
                BallInterruptAdapter adapter = (BallInterruptAdapter) xlistener;
                if(adapter.getBallSensedListener() == listener) {
                    return adapter;
                }
            }
        }
        return null;
    }
    
    /**
     * Wrapper class so that we don't expose to the commands that we are using
     * an InterruptWatcher behind the scenes. This is important so we can change
     * to a different back-end implementation later (such as a timer with polling)
     * and not need to modify the downstream classes.
     */
    private class BallInterruptAdapter implements InterruptWatcher.Listener {
        
        private final BallSensedListener _listener;
        public BallInterruptAdapter(BallSensedListener listener) {
            _listener = listener; 
        }
        
        public BallSensedListener getBallSensedListener() {
            return _listener;
        }
        
        public void interrupt(InterruptableSensorBase sensor) {
            _listener.ballSensed();
        }
    }
}
