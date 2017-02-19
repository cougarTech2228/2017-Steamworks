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
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;

public class Drive {
	private RobotDrive driveStyle;
	private Joystick joystick1;
	private Joystick joystick2;
	private CANTalon right1;
	private CANTalon left1;
	private CANTalon right2;
	private CANTalon left2;
	private Gear gear;
	private CANTalon gearArm;
	private CANTalon gearJaw;
	private boolean newButtonValue = false;
	private boolean oldButtonValue = false;
	private boolean driveType = false;
	private boolean baseLineBrake = false;
	private double startTime;
	private double gearValue;
	private boolean pressed;
	private double currentAngle;
	private int counter;
	private Gyro gyro;
	private double speedByTime;
	private double rotateValue;
	private double encoder;

	final double timeoutValue = 1.08; // seconds
	final double timeoutValueToLift = 1.1;
	
	private double timeOutValueSecondMove;
	double visionAngle;
	AHRS ahrs;
	private AnalogInput sonar;
	private int turnAngle;

	public enum Goal {
		DO_NOTHING, BASE_LINE_TIME_SENSOR, LEFT_BLUE, RIGHT_BLUE, LEFT_RED, RIGHT_RED, CENTER, MID, GEAR_PLACEMENT

	}

	public Goal autoGoal;

	public enum State {
		INIT, WAIT_FOR_TIME, DONE, TURN, GEAR_PLACEMENT, VISION_ALIGNMENT, VISION_SECOND, MOVE_TO_LIFT, PLACE_GEAR, BACK_UP
	}

	public State state;

	// Constructor
	public Drive() {
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

		gyro = new AnalogGyro(0);
		gyro.calibrate();
		currentAngle = 0;
		speedByTime = 0.33;
		rotateValue = 0;

		try {
			ahrs = new AHRS(SerialPort.Port.kUSB);
		} catch (RuntimeException ex) {
			System.out.println("Error starting the Nav-X");
		}
		ahrs.zeroYaw();
		sonar = new AnalogInput(1);
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit(AutoChoices autoSelected) {

		ahrs.zeroYaw();
		System.out.println("We are in AutoInit");
		right1.setPosition(0);
		left1.setPosition(0);
		switch (autoSelected) {

		case DO_NOTHING:
			System.out.println("Do Nothing");
			autoGoal = Goal.DO_NOTHING;
			break;
		case BASE_LINE_TIME_SENSOR:
			System.out.println("Base Line only");
			autoGoal = Goal.BASE_LINE_TIME_SENSOR;
			state = State.INIT;
			break;
		case LEFT_RED:
			System.out.println("Left Gear Red Placement");
			turnAngle = 60;
			autoGoal = Goal.GEAR_PLACEMENT;
			state = State.INIT;
			timeOutValueSecondMove = 1.4;
			break;
		case LEFT_BLUE:
			System.out.println("Left Gear Blue Placement");
			turnAngle = 60;
			timeOutValueSecondMove = 1.5;
			autoGoal = Goal.GEAR_PLACEMENT;
			state = State.INIT;
			break;
		case RIGHT_RED:
			System.out.println("Right Gear Red Placement");
			turnAngle = -60;
			autoGoal = Goal.GEAR_PLACEMENT;
			state = State.INIT;
			timeOutValueSecondMove = 1.11;
			state = State.INIT;
			break;
		case RIGHT_BLUE:
			System.out.println("Right Gear Blue Placement");
			turnAngle = -60;
			timeOutValueSecondMove = 1.5;
			autoGoal = Goal.GEAR_PLACEMENT;
			state = State.INIT;
			break;
		case CENTER:
			System.out.println("Center Gear Placement");
			turnAngle = 0;
			timeOutValueSecondMove = .6;
			autoGoal = Goal.GEAR_PLACEMENT;
			state = State.INIT;
		default: 

			break;
		}
	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic(Gear gear) {
		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		SmartDashboard.putNumber("SONAR", sonar.getValue());
		SmartDashboard.putNumber("RIGHT ENCODER COUNT", right1.getPosition());
		SmartDashboard.putNumber("LEFT ENCODER COUNT", left1.getPosition());

		// forcing it to be in BASE_LINE_TIME
		switch (autoGoal) {
		case DO_NOTHING:
			break;
		case BASE_LINE_TIME_SENSOR:
			if (state == State.INIT) {
				driveStyle.arcadeDrive(-0.15, 0, false);
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
					right1.set(0);
					left1.set(0);
					state = State.TURN;
					startTime = Timer.getFPGATimestamp();
					System.out.println(Timer.getFPGATimestamp());
				} else {
					chessyDriveAuto(speedByTime, rotateValue);
					System.out.println("HERE");

				}
			} else if (state == State.TURN) {
				if (turnAuto(60)) {
					state = State.DONE;
				}
			} else {

			}
			break;

		case GEAR_PLACEMENT:

			if (state == State.INIT) {
				ahrs.zeroYaw();
				state = State.WAIT_FOR_TIME;
				startTime = Timer.getFPGATimestamp();
				System.out.println("Start:");
				System.out.println(startTime);
				startTime += timeoutValue;


			} else if (state == State.WAIT_FOR_TIME) {
				
				if (Timer.getFPGATimestamp() >= (startTime + timeoutValue))
				{
					right1.set(0);
					left1.set(0);
					state = State.TURN;
					startTime = Timer.getFPGATimestamp();
					System.out.println(Timer.getFPGATimestamp());
				} else {
					chessyDriveAuto(-0.33, 0);
				}
			} else if (state == State.TURN) {

				
				
				if (turnAuto(turnAngle)) {

					if (Timer.getFPGATimestamp() >= (startTime + timeoutValue * 2)) {
						startTime = Timer.getFPGATimestamp();

						state = State.VISION_ALIGNMENT;
					}

				}
			} else if (state == State.VISION_ALIGNMENT) {

				if (visionAlignment()) {
					visionAngle = ahrs.getAngle();
					state = State.VISION_SECOND;
					startTime = Timer.getFPGATimestamp();
				}

			} else if (state == State.VISION_SECOND) {

				if (Timer.getFPGATimestamp() >= (startTime + timeoutValueToLift / 2.0)) {

					if (visionSecond()) {
						visionAngle = ahrs.getAngle();
						state = State.MOVE_TO_LIFT;
						startTime = Timer.getFPGATimestamp();

					}
					System.out.println("NOT PROBLEM");
				} else {
					driveStyle.arcadeDrive(0, 0, false);
					System.out.println("PROBLEM");
				}

			} else if (state == State.MOVE_TO_LIFT) {

				if (Timer.getFPGATimestamp() >= (startTime + timeOutValueSecondMove))

				{
					right1.set(0);
					left1.set(0);
					state = State.PLACE_GEAR;
					startTime = Timer.getFPGATimestamp();
					System.out.println("dun!");
					System.out.println(Timer.getFPGATimestamp());
				} else {
					moveToLift(gear, visionAngle);
				}

			} else if (state == State.PLACE_GEAR) {
				if (Timer.getFPGATimestamp() >= (startTime + timeOutValueSecondMove)) {

					startTime = Timer.getFPGATimestamp();
					state = State.BACK_UP;

				} else if (Timer.getFPGATimestamp() >= (startTime + timeoutValueToLift / 4)) {
					placeGearAuto(gear);
				} else {
					moveGearUp(gear);
				}

			} else if (state == State.BACK_UP) {
				if (Timer.getFPGATimestamp() >= (startTime + timeoutValueToLift))

				{
					right1.set(0);
					left1.set(0);
					state = State.DONE;
					startTime = Timer.getFPGATimestamp();
					System.out.println("dun!");
					System.out.println(Timer.getFPGATimestamp());
					ahrs.zeroYaw();
				} else {
					// chessyDriveAuto(-0.5, 0);
					moveAwayLift(visionAngle);
				}
			} else {

			}
		default:
			// System.out.println("at default");}
		}

	}

	// Called continuously during the teleop period
	public void teleopPeriodic() {

		// Press a button (7) to enter "chessyDrive" otherwise drive in
		// "tankDrive"
		// driveStyle.tankDrive(joystick1, joystick2);

		if (gearValue > 1) {
			
			gearValue = 1;

		} else if (gearValue < 0.4) {

			gearValue = .4;

		} else if (joystick2.getRawButton(RobotMap.JOY1_BUTTON_9_INCREASE_SPEED) && !pressed) {

			gearValue += .3;
			pressed = true;

		} else if (joystick2.getRawButton(RobotMap.JOY1_BUTTON_10_DECREASE_SPEED) && !pressed) {

			gearValue -= .3;
			ahrs.zeroYaw();
			pressed = true;
			
		} else if (!(joystick2.getRawButton(RobotMap.JOY1_BUTTON_9_INCREASE_SPEED) || joystick2.getRawButton(7)))

		{
			pressed = false;
		}

		changeDriveStyle();
		if (driveType == false) {
			chessyDrive(joystick2, 1, joystick1, 0);
			SmartDashboard.putString("Driving Mode", "ChessyDrive");
		} else {
			driveStyle.tankDrive(joystick1, joystick2);

			SmartDashboard.putString("Driving Mode", "TankDrive");
		}

	}

	public void testPeriodic() {

	}

	public Joystick getJoystick() {
		return joystick2;
	}

	public void chessyDrive(Joystick joys1, int axis1, Joystick joys2, int axis2) {

		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		double moveValue = (joys1.getRawAxis(1) * gearValue);
		double rotateValue = (joys1.getRawAxis(2) * -1) * gearValue;

		if (rotateValue < 0.1 && rotateValue > -0.1 && counter > 20) {

			if (ahrs.getAngle() > 1.5 + currentAngle) {

				rotateValue += .2;

			} else if (ahrs.getAngle() < -1.5 + currentAngle) {

				rotateValue -= .2;

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

	public void chessyDriveAuto(double moveValue, double rotateValue) {

		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1) {

				rotateValue += .2;

			} else if (ahrs.getAngle() < -1) {

				rotateValue -= .2;

			}

		}

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private boolean turnAuto(int optimalAngle) {

		double rotateValue = 0;
		
		if (ahrs.getAngle() > optimalAngle + 1) {

			rotateValue += .2;

		} else if (ahrs.getAngle() < optimalAngle - 1) {

			rotateValue -= .2;

		} else {
			
			return true;
		}

		driveStyle.arcadeDrive(0, rotateValue, false);
		return false;
	}

	private void moveToLift(Gear gear, double alignedAngle) {

		double rotateValue = 0;
		double moveValue = -.32;
		
		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1 + alignedAngle) {

				rotateValue += .2;

			} else if (ahrs.getAngle() < -1 + alignedAngle) {

				rotateValue -= .2;

			}

		}

		gear.gearArmSet(-.5);
		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private void placeGearAuto(Gear gear) {

		gear.gearClawSet(-0.4);
		gear.gearArmSet(0.1);

	}

	private void moveGearUp(Gear gear) {
		
		gear.gearArmSet(-0.3);
		
	}

	private void moveAwayLift(double alignedAngle) {

		double rotateValue = 0;
		double moveValue = +.32;
		
		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1 + alignedAngle) {

				rotateValue += .2;

			} else if (ahrs.getAngle() < -1 + alignedAngle) {

				rotateValue -= .2;
			}

		}

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private boolean visionAlignment() {

		if (SmartDashboard.getNumber("CenterX") > 185) {

			driveStyle.arcadeDrive(0, -0.2, false);

		} else if (SmartDashboard.getNumber("CenterX") < 135) {

			driveStyle.arcadeDrive(0, 0.2, false);

		} else {

			return true;
		}

		return false;
	}

	private boolean visionSecond() {

		if (SmartDashboard.getNumber("CenterX") > 170) {

			driveStyle.arcadeDrive(0, -0.15, false);

		} else if (SmartDashboard.getNumber("CenterX") < 150) {

			driveStyle.arcadeDrive(0, 0.15, false);

		} else {

			return true;
		}

		return false;
	}

	private void changeDriveStyle() {
		newButtonValue = joystick2.getRawButton(7);

		if (newButtonValue != oldButtonValue) {
			if (newButtonValue == true) {
				if (driveType == false) {
					// driveStyle.arcadeDrive(joystick2, 1, joystick1, 0);
					driveType = true;
				} else {
					// driveStyle.tankDrive(joystick1, joystick2);
					driveType = false;
				}

			}
			oldButtonValue = newButtonValue;
		}
	}
}