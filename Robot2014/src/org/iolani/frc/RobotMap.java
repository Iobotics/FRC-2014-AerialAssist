package org.iolani.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // drive train //
    public static final int driveLeftFrontPWM  = 2;
    public static final int driveLeftRearPWM   = 9;
    public static final int driveRightFrontPWM = 1;
    public static final int driveRightRearPWM  = 10;
    public static final int driveModeValve     = 1;
    
    // intake //
    public static final int intakeLeftPWM     = 3;
    public static final int intakeRightPWM    = 4;
    public static final int intakeDeployValve = 3;
    
    // ball grabber //
    public static final int ballGrabberValve = 2;
    public static final int ballSensorDIO    = 1;
    
    // catapult //
    public static final int catapultSwitchDIO  = 13;
    public static final int catapultSensorADC  = 1;
    public static final int catapultProngValve = 4;
    public static final int catapultWinchPWM   = 8;
    public static final int catapultWinchValve = 5;
    
    // penumatics //
    public static final int compressorRelay   = 8;
    public static final int pressureSwitchDIO = 14;
}
