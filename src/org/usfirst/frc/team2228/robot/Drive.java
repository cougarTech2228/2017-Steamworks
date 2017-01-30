package org.usfirst.frc.team2228.robot;

//Carrying over the classes from other libraries
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Enumeration;

public class Drive
{
	// Names of the classes
	private Timer myTime;
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
	private double startTime;
	private double gearValue;
	private boolean pressed;
	private Gyro gyro;

	public enum Goal
	{
		DO_NOTHING, BASE_LINE_TIME,

	}

	public Goal autoGoal;

	public enum State
	{
		INIT, WAIT_FOR_TIME, DONE
	}

	public State state;

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
		gearValue = .1;
		pressed = false;
		// creating a gear system
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
		autoGoal = Goal.DO_NOTHING;
		state = State.INIT;
		myTime = new Timer();
		gyro = new AnalogGyro(0);
		gyro.calibrate();
		// SmartDashboard.putData("GearValue", gearValue);

		// Hello person that is reading this. I am really bored and I don't know
		// what I'm doing. Please help me, I'm trapped and they won't let me
		// out. Please. PLE...
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit(String autoSelected)
	{
		System.out.println("We are in AutoInit");
		switch (autoSelected)
		{

			case ConstantMap.doNothing:
				System.out.println("I'm lazy");
				autoGoal = Goal.DO_NOTHING;
				break;
			case ConstantMap.baseLineTime:
				System.out.println("Now driving to Base Line only");
				autoGoal = Goal.BASE_LINE_TIME;
				right1.set(0.5);
				left1.set(0.5);
				state = State.WAIT_FOR_TIME;
				startTime = myTime.getFPGATimestamp();
				startTime += 1.5;
				break;
			default:

				break;
		}
	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic()
	{
		switch (autoGoal)
		{
			case DO_NOTHING:
				break;
			case BASE_LINE_TIME:
				if (state == State.WAIT_FOR_TIME)
				{
					if (myTime.getFPGATimestamp() >= startTime)
						;
					{
						right1.set(0);
						left1.set(0);
						state = State.DONE;
					}
				}

		}

	}

	// Called continuously during the teleop period
	public void teleopPeriodic()
	{

		// Press a button (7) to enter "chessyDrive" otherwise drive in
		// "tankDrive"
		// driveStyle.tankDrive(joystick1, joystick2);
		if (gearValue > 1)
		{

			gearValue = 1;

		}
		else if (gearValue < 0.4)
		{

			gearValue = .4;

		}
		else if (joystick2.getRawButton(8) && !pressed)
		{

			gearValue += .3;
			pressed = true;

		}
		else if (joystick2.getRawButton(7) && !pressed)
		{

			gearValue -= .3;
			pressed = true;

		}
		else if (!(joystick2.getRawButton(8) || joystick2.getRawButton(7)))
		{
			pressed = false;
		}

		changeDriveStyle();
		if (driveType == false)
		{
			// driveStyle.arcadeDrive(joystick2, 1, joystick1, 0);
			chessyDrive(joystick2, 1, joystick1, 0);
			SmartDashboard.putString("Driving Mode", "ArcadeDrive");
		}
		else
		{
			driveStyle.tankDrive(joystick1, joystick2);

			SmartDashboard.putString("Driving Mode", "TankDrive");
		}
	}

	public void testPeriodic()
	{

	}

	public Joystick getJoystick()
	{
		return joystick1;
	}

	public void chessyDrive(Joystick joys1, int axis1, Joystick joys2,
			int axis2)
	{

		double moveValue = joys1.getRawAxis(axis1);
		double rotateValue = -1 * joys2.getRawAxis(axis2);
		// double moveValue = (joys1.getRawAxis(1)*gearValue);
		// double rotateValue = (joys1.getRawAxis(2)*-1)*gearValue;

		// if(rotateValue < 0.1 && rotateValue > -0.1){
		//
		// if(gyro.getAngle()>3){
		//
		// rotateValue-=.3;
		//
		// }else if(gyro.getAngle()<-3){
		//
		// rotateValue+=.3;
		//
		// }
		//
		// }

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private void changeDriveStyle()
	{
		// newButtonValue = joystick2.getRawButton(7);

		if (newButtonValue != oldButtonValue)
		{
			if (newButtonValue == true)
			{
				if (driveType == false)
				{
					// driveStyle.arcadeDrive(joystick2, 1, joystick1, 0);
					driveType = true;
				}
				else
				{
					// driveStyle.tankDrive(joystick1, joystick2);
					driveType = false;
				}

			}
			oldButtonValue = newButtonValue;
		}
	}
}