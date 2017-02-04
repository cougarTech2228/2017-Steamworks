package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CameraServer;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot
{
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	private AutoChoices choisir;
	// Carrying the classes from this project's library
	private Gear gear;
	private Balls balls;
	private Climb climb;
	private Drive drive;
	private CANTalon shooter;
	private ConstantMap constant;
	private RobotMap map;
	// SendableChooser<String> chooser = new SendableChooser<>();
	SendableChooser<ConstantMap.AutoChoices> chooser = new SendableChooser<>();

	/*
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	@Override
	public void robotInit()
	{
		// chooser.addDefault("Default Auto", defaultAuto);
		// chooser.addObject("My Auto", customAuto);
		chooser.addDefault("Do Nothing", ConstantMap.AutoChoices.DO_NOTHING);
		chooser.addObject("Base Line", ConstantMap.AutoChoices.BASE_LINE_TIME);
		SmartDashboard.putData("Auto choices", chooser);
		// balls = new Balls();
		drive = new Drive();
		//climb = new Climb(drive.getJoystick());
		 gear = new Gear(drive.getJoystick());

		 climb = new Climb(drive.getJoystick());
		 gear = new Gear(drive.getJoystick());
		// shooter = new CANTalon(RobotMap.RIGHT_SHOOTER_ONE);
		SmartDashboard.putString("autonomous selection", ConstantMap.doNothing);
//		CameraServer.getInstance().startAutomaticCapture();

	}

	/*
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit()
	{
		choisir = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
//		autoSelected = SmartDashboard.getString("autonomous selection",
//				ConstantMap.doNothing);
		drive.autonomousInit(choisir);
		System.out.println(autoSelected);
		if (choisir == AutoChoices.DO_NOTHING)
		{
			System.out.println("No Choosing 4 u");
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic()
	{
		// System.out.println("You have reached autonomousPeriodic");
		drive.autonomousPeriodic();
		/*
		 * // Put custom auto code here break; case defaultAuto: default: // Put
		 * default auto code here break; }
		 */
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		// Calling the code from the drive class
		drive.teleopPeriodic();
		// shooter.set(.8);
//		 climb.teleopPeriodic();
		 gear.teleopPeriodic();
		 climb.teleopPeriodic();
		 gear.teleopPeriodic();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic()
	{
	}
}

//import com.kauailabs.navx.frc.AHRS;
//
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.I2C;
//import edu.wpi.first.wpilibj.SerialPort;
//import edu.wpi.first.wpilibj.SampleRobot;
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program providing a real-time display of navX-MXP values.
 *
 * In the operatorControl() method, all data from the navX-MXP is retrieved
 * and output to the SmartDashboard.
 *
 * The output data values include:
 *
 * - Yaw, Pitch and Roll angles
 * - Compass Heading and 9-Axis Fused Heading (requires Magnetometer calibration)
 * - Linear Acceleration Data
 * - Motion Indicators
 * - Estimated Velocity and Displacement
 * - Quaternion Data
 * - Raw Gyro, Accelerometer and Magnetometer Data
 *
 * As well, Board Information is also retrieved; this can be useful for debugging
 * connectivity issues after initial installation of the navX-MXP sensor.
 *
 */
//public class Robot extends SampleRobot {
//  AHRS ahrs;
//  Joystick stick;
//
//  public Robot() {
//      stick = new Joystick(0);
//      try {
//          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
//          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
//          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
//          ahrs = new AHRS(SPI.Port.kMXP); 
//      } catch (RuntimeException ex ) {
//          DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
//      }
//  }
//
//  /**
//   * Runs during autonomous mode
//   */
//  public void autonomous() {
//      Timer.delay(2.0);		//    for 2 seconds
//  }
//
//  /**
//   * Display navX-MXP Sensor Data on Smart Dashboard
//   */
//  public void operatorControl() {
//      while (isOperatorControl() && isEnabled()) {
//          
//          Timer.delay(0.020);		/* wait for one motor update time period (50Hz)     */
//          
//          boolean zero_yaw_pressed = stick.getTrigger();
//          if ( zero_yaw_pressed ) {
//              ahrs.zeroYaw();
//          }
//
//          /* Display 6-axis Processed Angle Data                                      */
//          SmartDashboard.putBoolean(  "IMU_Connected",        ahrs.isConnected());
//          SmartDashboard.putBoolean(  "IMU_IsCalibrating",    ahrs.isCalibrating());
//          SmartDashboard.putNumber(   "IMU_Yaw",              ahrs.getYaw());
//          SmartDashboard.putNumber(   "IMU_Pitch",            ahrs.getPitch());
//          SmartDashboard.putNumber(   "IMU_Roll",             ahrs.getRoll());
//          
//          /* Display tilt-corrected, Magnetometer-based heading (requires             */
//          /* magnetometer calibration to be useful)                                   */
//          
//          SmartDashboard.putNumber(   "IMU_CompassHeading",   ahrs.getCompassHeading());
//          
//          /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
//          SmartDashboard.putNumber(   "IMU_FusedHeading",     ahrs.getFusedHeading());
//
//          /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
//          /* path for upgrading from the Kit-of-Parts gyro to the navx-MXP            */
//          
//          SmartDashboard.putNumber(   "IMU_TotalYaw",         ahrs.getAngle());
//          SmartDashboard.putNumber(   "IMU_YawRateDPS",       ahrs.getRate());
//
//          /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */
//          
//          SmartDashboard.putNumber(   "IMU_Accel_X",          ahrs.getWorldLinearAccelX());
//          SmartDashboard.putNumber(   "IMU_Accel_Y",          ahrs.getWorldLinearAccelY());
//          SmartDashboard.putBoolean(  "IMU_IsMoving",         ahrs.isMoving());
//          SmartDashboard.putBoolean(  "IMU_IsRotating",       ahrs.isRotating());
//
//          /* Display estimates of velocity/displacement.  Note that these values are  */
//          /* not expected to be accurate enough for estimating robot position on a    */
//          /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
//          /* of these errors due to single (velocity) integration and especially      */
//          /* double (displacement) integration.                                       */
//          
//          SmartDashboard.putNumber(   "Velocity_X",           ahrs.getVelocityX());
//          SmartDashboard.putNumber(   "Velocity_Y",           ahrs.getVelocityY());
//          SmartDashboard.putNumber(   "Displacement_X",       ahrs.getDisplacementX());
//          SmartDashboard.putNumber(   "Displacement_Y",       ahrs.getDisplacementY());
//          
//          /* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
//          /* NOTE:  These values are not normally necessary, but are made available   */
//          /* for advanced users.  Before using this data, please consider whether     */
//          /* the processed data (see above) will suit your needs.                     */
//          
//          SmartDashboard.putNumber(   "RawGyro_X",            ahrs.getRawGyroX());
//          SmartDashboard.putNumber(   "RawGyro_Y",            ahrs.getRawGyroY());
//          SmartDashboard.putNumber(   "RawGyro_Z",            ahrs.getRawGyroZ());
//          SmartDashboard.putNumber(   "RawAccel_X",           ahrs.getRawAccelX());
//          SmartDashboard.putNumber(   "RawAccel_Y",           ahrs.getRawAccelY());
//          SmartDashboard.putNumber(   "RawAccel_Z",           ahrs.getRawAccelZ());
//          SmartDashboard.putNumber(   "RawMag_X",             ahrs.getRawMagX());
//          SmartDashboard.putNumber(   "RawMag_Y",             ahrs.getRawMagY());
//          SmartDashboard.putNumber(   "RawMag_Z",             ahrs.getRawMagZ());
//          SmartDashboard.putNumber(   "IMU_Temp_C",           ahrs.getTempC());
//          
//          /* Omnimount Yaw Axis Information                                           */
//          /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
//          AHRS.BoardYawAxis yaw_axis = ahrs.getBoardYawAxis();
//          SmartDashboard.putString(   "YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
//          SmartDashboard.putNumber(   "YawAxis",              yaw_axis.board_axis.getValue() );
//          
//          /* Sensor Board Information                                                 */
//          SmartDashboard.putString(   "FirmwareVersion",      ahrs.getFirmwareVersion());
//          
//          /* Quaternion Data                                                          */
//          /* Quaternions are fascinating, and are the most compact representation of  */
//          /* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
//          /* from the Quaternions.  If interested in motion processing, knowledge of  */
//          /* Quaternions is highly recommended.                                       */
//          SmartDashboard.putNumber(   "QuaternionW",          ahrs.getQuaternionW());
//          SmartDashboard.putNumber(   "QuaternionX",          ahrs.getQuaternionX());
//          SmartDashboard.putNumber(   "QuaternionY",          ahrs.getQuaternionY());
//          SmartDashboard.putNumber(   "QuaternionZ",          ahrs.getQuaternionZ());
//          
//          /* Connectivity Debugging Support                                           */
//          SmartDashboard.putNumber(   "IMU_Byte_Count",       ahrs.getByteCount());
//          SmartDashboard.putNumber(   "IMU_Update_Count",     ahrs.getUpdateCount());
//      }
//  }
//
//  /**
//   * Runs during test mode
//   */
//  public void test() {
//  }
//}
