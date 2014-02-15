package org.iolani.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // drive train //
    public static final int driveLeftFrontPWM  = 10;
    public static final int driveLeftRearPWM   = 9;
    public static final int driveRightFrontPWM = 1;
    public static final int driveRightRearPWM  = 2;
    public static final int driveValve1        = 1;
    public static final int driveValve2        = 2;

    // intake //
    public static final int intakeLeftPWM = 3;
    public static final int intakeRightPWM = 4;
    
    // ball grabber //
    public static final int ballGrabberValve1 = 3;
    public static final int ballGrabberValve2 = 4;
    public static final int ballSensorDIO     = 13;
    
    // catapult //
    public static final int catapultLatchValve = 5;
    
    // penumatics //
    public static final int compressorRelay   = 1;
    public static final int pressureSwitchDIO = 1;
}
