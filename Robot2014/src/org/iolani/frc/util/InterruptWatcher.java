package org.iolani.frc.util;

import edu.wpi.first.wpilibj.InterruptableSensorBase;
import java.util.Enumeration;
import java.util.Vector;

/**
 *  Class to watch for interrupts from InterruptableSensorBase classes
 *  and notify listeners.
 * 
 * @author jmalins
 */
public class InterruptWatcher {
    // watcher states //
    public static final int sSTOPPED  = 1;
    public static final int sSTOPPING = 2;
    public static final int sRUNNING  = 3;
    
    /**
     * Default wait timeout (1ms).
     */
    public static final int DEFAULT_WAIT_MS = 1;
    
    private final InterruptableSensorBase _sensor;
    private final Thread                  _thread;
    private final double                  _waitTime;
    private final Vector                  _listeners;
    
    private int    _state     = sSTOPPED;
    private double _timestamp;
    
    /**
     * Create a watcher for the specified sensor. Interrupts should be configured
     * and/or requested on the sensor before the watcher is started. The watcher
     * handles the enabling or disabling of interrupts, so do not do this manually.
     * 
     * @param sensor - the sensor
     * @param wait_ms - the default time to wait, impacts how long stop() takes
     */
    public InterruptWatcher(InterruptableSensorBase sensor, int wait_ms) {
        if(sensor == null) {
            throw new IllegalArgumentException();
        }
        _sensor    = sensor;
        _thread    = new Thread(new Runner());
        _waitTime  = wait_ms / 1000.0;
        _listeners = new Vector();
        
        _timestamp = sensor.readInterruptTimestamp();
    }

    /**
     * Create a watcher for the specified sensor with the default wait time. 
     * Interrupts should be configured and/or requested on the sensor before the 
     * watcher is started. The watcher handles the enabling or disabling of 
     * interrupts, so do not do this manually.
     * 
     * @param sensor - the sensor
     */
    public InterruptWatcher(InterruptableSensorBase sensor) {
        this(sensor, DEFAULT_WAIT_MS);
    }
    
    /**
     * Start the watcher.
     */
    public void start() {
        // must use monitor synchronization instead of method synchronization //
        // since we want both start() and stop() to be mutually exclusive.    //
        synchronized(this) {
            if(!_thread.isAlive()) _thread.start();
        }
    }
    
    /**
     * Stop the watcher and wait for the watch loop to terminate. This will take
     * a maximum of WaitTimeMs. 
     */
    public void stop() {
        // must use monitor synchronization instead of method synchronization //
        // since we want both start() and stop() to be mutually exclusive.    //
        synchronized(this) {
            if(_state == sRUNNING) {
                _state = sSTOPPING;
                try {
                    _thread.join();
                } catch(InterruptedException e) {
                    // do nothing, we will be stopped //
                }
            }
        }
    }
    
    /**
     * Get the state of the watcher.
     * @return the state (sSTOPPED, sSTOPPING, sRUNNING)
     */
    public int getState() {
        return _state;
    }
    
    /**
     * Add a listener to this watcher.
     * @param listener - the listener to add
     */
    public void addListener(Listener listener) {
        if(!_listeners.contains(listener)) {
            _listeners.addElement(listener);
        }
    }
    
    /**
     * Remove a listener from this watcher.
     * @param listener - the listener to remove
     * @return true if the listener was present, otherwise false
     */
    public boolean removeListener(Listener listener) {
        return _listeners.removeElement(listener);
    }
    
    /**
     * Get the number of registered listeners.
     * @return count of registered listeners.
     */
    public int getListenerCount() {
        return _listeners.size();
    }
    
    /**
     * Get the registered listeners.
     * @return an Enumeration of registered listeners.
     */
    public Enumeration enumerateListeners() {
        return _listeners.elements();
    }
    
    /**
     * Listener interface.
     */
    public interface Listener {
        void interrupt(InterruptableSensorBase sensor);
    }
    
    // inner class to handle the threading //
    private class Runner implements Runnable {
        public void run() {
            _state = sRUNNING;
            _sensor.enableInterrupts();
            while(_state == sRUNNING) {
                _sensor.waitForInterrupt(_waitTime);
                // we don't get the results of the interrupt, so check //
                // if timestamp changed while we were waiting          //
                double ts = _sensor.readInterruptTimestamp();
                if(ts != _timestamp) {
                    // notify the listeners //
                    Enumeration e = _listeners.elements();
                    while(e.hasMoreElements()) {
                        ((Listener) e.nextElement()).interrupt(_sensor);
                    }
                    _timestamp = ts;
                }
            }
            _sensor.disableInterrupts();
            _state = sSTOPPED;
        }
    }   
}
