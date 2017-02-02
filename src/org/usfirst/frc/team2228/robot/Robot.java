package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
	// Carrying the classes from this project's library
	private Gear gear;
	private Balls balls;
	private Climb climb;
	private Drive drive;
	private CANTalon shooter;
	private ConstantMap constant;
	private RobotMap map;
	SendableChooser<String> chooser = new SendableChooser<>();

	/*
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	@Override
	public void robotInit()
	{
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		chooser.addObject("Do Nothing", ConstantMap.doNothing);
		chooser.addObject("Base Line", ConstantMap.baseLineTime);
		SmartDashboard.putData("Auto choices", chooser);
		//balls = new Balls();
		drive = new Drive();
		//climb = new Climb(drive.getJoystick());
		// gear = new Gear(drive.getJoystick());

		// shooter = new CANTalon(RobotMap.RIGHT_SHOOTER_ONE);
		SmartDashboard.putString("autonomous selection", ConstantMap.doNothing);
		CameraServer.getInstance().startAutomaticCapture();

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
		//autoSelected = chooser.getSelected();
		//autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		autoSelected = SmartDashboard.getString("autonomous selection", ConstantMap.doNothing);
		drive.autonomousInit(autoSelected);
		System.out.println(autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic()
	{
		//System.out.println("You have reached autonomousPeriodic");
		drive.autonomousPeriodic();
		/*
		 * switch (autoSelected) { case doNothing:
		 * 
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
		// climb.teleopPeriodic();
		// gear.teleopPeriodic();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic()
	{
	}
}
