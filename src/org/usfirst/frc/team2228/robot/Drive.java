package org.usfirst.frc.team2228.robot;

//Carrying over the classes from other libraries
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;

public class Drive
{
	private RobotDrive driveStyle;
	private Joystick joystick1;
	private CANTalon right1;
	private CANTalon left1;
	private CANTalon right2;
	private CANTalon left2;

	private Gear gear;
	
	private boolean newButtonValue = false;
	private boolean oldButtonValue = false;
	private boolean driveType = false;
	private boolean baseLineBrake = false;
	private double beginningEncoderCount;
	private double finalEncoderCount;
	private double startTime;
	private double gearValue;
	private boolean pressed;
	private double currentAngle;
	private int counter;
	private Gyro gyro;
	private double speedByTime;
	private double rotateValue;
	private double encoder;
	private int largeAngle = 60;
	private int smallAngle = 45;
	private double competitionGearPlacementDrivingSpeed = 0.5;
	private double testBotleftBlueTimeoutValueSecondMove = 1.5;
	private double speedIncreaseXbox;
	private double speedDecreaseXbox;
	private double speedIncrease = 0.3;
	private double speedDecrease = 0.3;
	private double rotateIncrease = 0.22;
	private double rotateDecrease = 0.22;
	final int testBotRightEncoder = 4900;
	final int testBotLeftEncoder = -3200;
	int testBotRightMoveToLiftEncoder = 3000;
	int testBotLeftMoveToLiftEncoder = -550;

	final double timeoutValue = 1.08; // seconds
	final double timeoutValueToLift = 1.1;
	
	final double testBotTimeoutValue = 4; // seconds
	final double testBotTimeoutValueToLift = 0.54;

	private double timeOutValueSecondMove;
	double visionAngle;
	AHRS ahrs;
	private AnalogInput sonar;
	private int turnAngle;
	public enum Goal
	{
		DO_NOTHING, BASE_LINE, TURN_GEAR_PLACEMENT, VISION_PLACEMENT, CENTER
	}

	public Goal autoGoal;

	public enum State
	{
		INIT, WAIT_FOR_TIME, DONE, TURN, GEAR_PLACEMENT, VISION_ALIGNMENT, VISION_SECOND, MOVE_TO_LIFT, PLACE_GEAR, BACK_UP
	}

	public State state;

	// Constructor

	public Drive(Gear gear, Joystick joystick) {
		// "Creating" the objects
		// Create the four motor controller objects for the drive base
		right1 = new CANTalon(RobotMap.RIGHT_ONE_DRIVE); 
		right2 = new CANTalon(RobotMap.RIGHT_TWO_DRIVE); 
		left1 = new CANTalon(RobotMap.LEFT_ONE_DRIVE); 
		left2 = new CANTalon(RobotMap.LEFT_TWO_DRIVE); 

		gearValue = .1;
		pressed = false;
		// creating a gear system
		joystick1 = joystick;
		// Create the RobotDrive object
		driveStyle = new RobotDrive(right1, left1);

		right1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		left1.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		// Set left2 and right2 to follow the commands of left1 and left2
		right2.changeControlMode(TalonControlMode.Follower);
		right2.enableControl();
		right2.set(right1.getDeviceID());
		left2.changeControlMode(TalonControlMode.Follower);
		left2.enableControl();
		left2.set(left1.getDeviceID());
		autoGoal = Goal.DO_NOTHING;
		state = State.INIT;

		currentAngle = 0;
		speedByTime = -0.33;
		rotateValue = 0;

		try
		{
			ahrs = new AHRS(SerialPort.Port.kUSB);
		}
		catch (RuntimeException ex)
		{
			System.out.println("Error starting the Nav-X");
		}
		ahrs.zeroYaw();

	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit(AutoChoices autoSelected)
	{

		currentAngle = ahrs.getYaw();
		System.out.println("We are in AutoInit");
		right1.setPosition(0);
		left1.setPosition(0);
		switch (autoSelected)
		{

			case DO_NOTHING:
				System.out.println("Do Nothing");
				autoGoal = Goal.DO_NOTHING;
				break;
				
			case BASE_LINE_TIME_SENSOR:
				System.out.println("Base Line only");
				autoGoal = Goal.BASE_LINE;
				state = State.INIT;
				break;
				
			case RIGHT_GEAR_PLACEMENT:
				System.out.println("Right Gear Placement");
				turnAngle = -largeAngle;
				timeOutValueSecondMove = testBotleftBlueTimeoutValueSecondMove;
				autoGoal = Goal.TURN_GEAR_PLACEMENT;
				state = State.INIT;
				break;
				
			case LEFT_GEAR_PLACEMENT:
				System.out.println("Left Gear Placement");
				turnAngle = largeAngle;
				timeOutValueSecondMove = testBotleftBlueTimeoutValueSecondMove;
				autoGoal = Goal.TURN_GEAR_PLACEMENT;
				state = State.INIT;
				testBotLeftMoveToLiftEncoder = 0;
				break;
				
			case CENTER:
				System.out.println("Center Gear Placement");
				turnAngle = 0;
				timeOutValueSecondMove = .6;
				autoGoal = Goal.TURN_GEAR_PLACEMENT;
				state = State.INIT;
				break;
				
			case VISION_GEAR_LEFT:
				System.out.println("vision left Gear Placement");
				turnAngle = 0;
				timeOutValueSecondMove = .6;
				autoGoal = Goal.TURN_GEAR_PLACEMENT;
				state = State.INIT;
				break;
				
			case VISION_GEAR_RIGHT:
				System.out.println("vision right Gear Placement");
				turnAngle = 0;
				timeOutValueSecondMove = .6;
				autoGoal = Goal.TURN_GEAR_PLACEMENT;
				state = State.INIT;
				break;

			default:

		}
	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic(Gear gear)
	{
		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
//		SmartDashboard.putNumber("SONAR", sonar.getValue());
		SmartDashboard.putNumber("RIGHT ENCODER COUNT", right1.getPosition());
		SmartDashboard.putNumber("LEFT ENCODER COUNT", left1.getPosition());

		// forcing it to be in BASE_LINE_TIME

		switch (autoGoal) {
		case DO_NOTHING:
			break;
		case BASE_LINE_TIME_SENSOR:
			if (state == State.INIT) {
                gear.gearArmSet(-0.5);
				state = State.WAIT_FOR_TIME;
				startTime = Timer.getFPGATimestamp();
				System.out.println("Start:");
				System.out.println(startTime);
				startTime += timeoutValue;
				System.out.println("end:");
				System.out.println(startTime);
			} else if (state == State.WAIT_FOR_TIME) {
				if (Timer.getFPGATimestamp() >= (startTime + timeoutValue))
				{
					chessyDriveAuto(0, 0);
					state = State.TURN;
					startTime = Timer.getFPGATimestamp();
					System.out.println(Timer.getFPGATimestamp());
				} else {
					chessyDriveAuto(speedByTime, rotateValue);
				}
			} else if (state == State.TURN) {
				if (turnAuto(60)) {
					chessyDriveAuto(0, 0);
					state = State.DONE;
				}
			} 
			break;

		case TURN_GEAR_PLACEMENT:

			if (state == State.INIT) {
				gear.gearArmSet(-0.5);
				state = State.WAIT_FOR_TIME;
				startTime = Timer.getFPGATimestamp();
				System.out.println("Start:");
				System.out.println(startTime);
				startTime += timeoutValue;

			} else if (state == State.WAIT_FOR_TIME) {

					if (Timer.getFPGATimestamp() >= (startTime
							+ testBotTimeoutValue))
					{
						chessyDriveAuto(0, 0);
						state = State.TURN;
						startTime = Timer.getFPGATimestamp();
						System.out.println(Timer.getFPGATimestamp());
					}
					else if (right1.getPosition() >= testBotRightEncoder
							|| left1.getPosition() <= testBotLeftEncoder)
					{
						System.out.println(right1.getPosition());
						System.out.println(left1.getPosition());
						chessyDriveAuto(0, 0);
						state = State.TURN;

					}
					else
					{
						chessyDriveAuto(speedByTime, rotateValue);
					}
				}
				else if (state == State.TURN)
				{
					if (turnAuto(60))
					{
						state = State.GEAR_PLACEMENT;
						right1.setPosition(0);
						left1.setPosition(0);
						ahrs.zeroYaw();
					}
				}
				else if (state == State.GEAR_PLACEMENT)
				{
					state = State.MOVE_TO_LIFT;
					startTime = Timer.getFPGATimestamp();
					System.out.println("Start:");
					System.out.println(startTime);
					startTime += testBotTimeoutValueToLift;
					System.out.println("end:");
					System.out.println(startTime);
				}
				else if (state == State.MOVE_TO_LIFT)
				{
					if (Timer.getFPGATimestamp() >= (startTime
							+ testBotTimeoutValueToLift))
					{
						chessyDriveAuto(0, 0);
						startTime = Timer.getFPGATimestamp();
						System.out.println(Timer.getFPGATimestamp());
					}
					if (right1.getPosition() >= testBotRightMoveToLiftEncoder
							|| left1.getPosition() <= testBotLeftMoveToLiftEncoder)
					{
						System.out.println(right1.getPosition());
						System.out.println(left1.getPosition());
						chessyDriveAuto(0, 0);
						placeGearAuto(gear);
						moveGearUp(gear);
						state = State.DONE;
					}
					else
					{
						chessyDriveAuto(speedByTime, rotateValue);
					}
				}

		        default:
				 System.out.println("at default");
			}


	}

	// Called continuously during the teleop period

	public void teleopPeriodic() {
		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());

		if (ahrs.getRoll() > 100 || ahrs.getRoll() < 80) {
			System.out.println("Done Borked");
		}

		
		speedIncreaseXbox = joystick1.getRawAxis(3);
		System.out.println(speedIncreaseXbox);
		speedDecreaseXbox = joystick1.getRawAxis(2);
		

		if (gearValue > 1) {
			gearValue = 1;

		} else if (gearValue < 0.2) {
			gearValue = .2;


		} else if ((joystick1.getRawAxis(3) > .8) && !pressed) {
			gearValue += .2;

			pressed = true;


		} else if (joystick1.getRawAxis(2) > .8 && !pressed) {

			gearValue -= .2;
			// ahrs.zeroYaw();
			pressed = true;

		} else if (!(joystick1.getRawAxis(3) > .8) || joystick1.getRawAxis(2) > .8)


		{
			pressed = false;
		}

		chessyDrive(joystick1, 1, joystick1, 0);

	}

	public void testPeriodic()
	{

	}


	public Joystick getJoystick() {
		return joystick1;

	}

	public void chessyDrive(Joystick joys1, int axis1, Joystick joys2,
			int axis2)
	{

		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		double moveValue = (joys1.getRawAxis(1) * gearValue);
		double rotateValue = (joys1.getRawAxis(4) * -1) * gearValue;

		if (rotateValue < 0.1 && rotateValue > -0.1 && counter > 20)
		{

			if (ahrs.getAngle() > 3 + currentAngle)
			{


				rotateValue += .15;

			}
			else if (ahrs.getAngle() < -3 + currentAngle)
			{


				rotateValue -= .15;

			}


		} else if (counter <= 20 && rotateValue < 0.1 && rotateValue > -0.1) {

			currentAngle = ahrs.getAngle();
			counter++;


		} else {


			currentAngle = ahrs.getAngle();
			counter = 0;

		}

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	public void chessyDriveAuto(double moveValue, double rotateValue)
	{

		if (rotateValue < 0.1 && rotateValue > -0.1)
		{

			if (ahrs.getAngle() > 1) {

				rotateValue += .1;

			}
			else if (ahrs.getYaw() < -1.5 + currentAngle)
			{

				rotateValue -= .1;

			}

		}
		
		
		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private boolean turnAuto(int optimalAngle)
	{
		double rotateValue = 0;
		
		if (ahrs.getAngle() > optimalAngle + 2) {

			rotateValue += rotateIncrease;
		}
		else if (ahrs.getAngle() < optimalAngle - 2) {
			
			rotateValue -= rotateDecrease;

		}
		else
		{
			return true;
		}

		driveStyle.arcadeDrive(0, rotateValue, false);
		return false;
	}

	private void moveToLift(Gear gear, double alignedAngle)
	{
		double rotateValue = 0;
		double moveValue = -.32;

		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1 + alignedAngle)
			{
				rotateValue += rotateIncrease;

			}
			else if (ahrs.getAngle() < -1 + alignedAngle)
			{
				rotateValue -= rotateDecrease;

			}

		}

		gear.gearArmSet(-.5);
		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private void placeGearAuto(Gear gear)
	{

		gear.gearClawSet(-0.4);
		gear.gearArmSet(0.1);

	}


	private void moveGearUp(Gear gear) {
		gear.gearArmSet(-0.3);

	}

	private void moveAwayLift(double alignedAngle)
	{

		double rotateValue = 0;
		double moveValue = +.32;

		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1 + alignedAngle)
			{
				rotateValue += rotateIncrease;

			}
			else if (ahrs.getAngle() < -1 + alignedAngle)
			{
				rotateValue -= rotateDecrease;
			}

		}

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private boolean visionAlignment()
	{

		if (SmartDashboard.getNumber("CenterX") > 185)
		{

			driveStyle.arcadeDrive(0, -0.25, false);

		}
		else if (SmartDashboard.getNumber("CenterX") < 135)
		{

			driveStyle.arcadeDrive(0, 0.25, false);

		}
		else
		{

			return true;
		}

		return false;
	}

	private boolean visionSecond()
	{

		if (SmartDashboard.getNumber("CenterX") > 170)
		{

			driveStyle.arcadeDrive(0, -0.25, false);

		}
		else if (SmartDashboard.getNumber("CenterX") < 150)
		{

			driveStyle.arcadeDrive(0, 0.25, false);

		}
		else
		{

			return true;
		}

		return false;
	}

	
}
