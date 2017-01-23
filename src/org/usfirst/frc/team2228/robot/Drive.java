package org.usfirst.frc.team2228.robot;

//Carrying over the classes from other libraries
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Enumeration;

public class Drive
{
	// Names of the classes
	private RobotDrive driveStyle;
	private Joystick joystick1;
	private Joystick joystick2;
	private CANTalon right1;
	private CANTalon left1;
	private CANTalon right2;
	private CANTalon left2;
	private RobotMap map;
	private ConstantMap constant;
	private boolean newButtonValue = false;
	private boolean oldButtonValue = false;
	private boolean driveType = false;

	public enum Goal
	{
		DO_NOTHING, FORWARD_BY_TIME,

	}

	// Constructor
	public Drive()
	{
		// "Creating" the objects
		// Create the four motor controller objects for the drive base
		right1 = new CANTalon(RobotMap.RIGHT_ONE_DRIVE); // 2
		right2 = new CANTalon(RobotMap.RIGHT_TWO_DRIVE); // 1
		left1 = new CANTalon(RobotMap.LEFT_ONE_DRIVE); // 3
		left2 = new CANTalon(RobotMap.LEFT_TWO_DRIVE); // 4
		// Creating the joystick objects
		joystick1 = new Joystick(0);
		joystick2 = new Joystick(1);
		// Create the RobotDrive object
		driveStyle = new RobotDrive(right1, left1);
		driveStyle.tankDrive(joystick1, joystick2);
		// Set left2 and right2 to follow the commands of left1 and left2
		right2.changeControlMode(TalonControlMode.Follower);
		right2.enableControl();
		right2.set(right1.getDeviceID());
		left2.changeControlMode(TalonControlMode.Follower);
		left2.enableControl();
		left2.set(left1.getDeviceID());
	

	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit(String autoSelected)
	{
		switch (autoSelected)
		{

			case ConstantMap.doNothing:

				// Put custom auto code here
				break;
			default:
				// Put default auto code here
				break;
		}
	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic()
	{

	}

	// Called continuously during the teleop period
	public void teleopPeriodic()
	{

		// Press a button (7) to enter "chessyDrive" otherwise drive in
		// "tankDrive"
		driveStyle.tankDrive(joystick1, joystick2);
//		newButtonValue = joystick2.getRawButton(7);
//
//		if (newButtonValue != oldButtonValue)
//		{
//			if (newButtonValue == true)
//			{
//				if (driveType == false)
//				{
//					driveStyle.arcadeDrive(joystick2, 1, joystick1, 0);
//					driveType = true;
//				}
//				else
//				{
//					driveStyle.tankDrive(joystick1, joystick2);
//					driveType = false;
//				}
//
//			}
//			oldButtonValue = newButtonValue;
//		}

	}

	public void testPeriodic()
	{

	}

	public Joystick getJoystick()
	{
		return joystick1;
	}
}