package org.usfirst.frc.team2228.robot;

//Carrying over the classes from other libraries
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;

public class Drive {
	private double xSpeedDeadBand = .1;
	public static final int kLowSmooth = 5;
	public static final int kHighSmooth = 35;
	public int timePeriodSF = 35;
	private int xZero = 0;
	private RobotDrive driveStyle;
	private Joystick joystick1;
	private CANTalon right1;
	private CANTalon left1;
	private CANTalon right2;
	private CANTalon left2;
	private Fuel fuelClass;
	private Gear gear;
	private boolean newButtonValue = false;
	private boolean oldButtonValue = false;
	private double startTime;
	private double gearValue;
	private boolean pressed;
	private double currentAngle;
	private int counter;
	private double speedByTime;
	private double rotateValue;
	private double encoder;
	private int largeAngle = 60;
	private double testBotleftBlueTimeoutValueSecondMove = 1.5;
	private double speedIncreaseXbox;
	private double speedDecreaseXbox;
	private double speedIncrease = 0.1;
	private double speedDecrease = 0.1;
	private double rotateIncrease = 0.31;
	private double rotateDecrease = 0.31;
	private double timeOutValueSecondMove;
	private BuiltInAccelerometer accel;
	// final int testBotRightEncoder = 4900;
	// final int testBotLeftEncoder = -3200; what was on lambda
	final int testBotRightEncoder = 4900 * 2;
	final int testBotLeftEncoder = -2400 * 2;

	int testBotRightMoveToLiftEncoder = 3000;
	int testBotLeftMoveToLiftEncoder = -550;
	private double oldEMA;
	final double timeoutValue = 1.08; // seconds
	final double timeoutValueToLift = 1.1;
	final double testBotTimeoutValue = 4; // seconds
	final double testBotTimeoutValueToLift = 0.54;
	double visionAngle;
	AHRS ahrs;
	private AnalogInput sonar;
	private int turnAngle;

	public enum Goal {
		DO_NOTHING, TURN_GEAR_PLACEMENT, VISION_PLACEMENT, CENTER, BASE_LINE, GEAR_PLACEMENT, GEAR_PLACEMENT_DREAM, VISION_GEAR_RIGHT, VISION_GEAR_LEFT, GEAR_AND_FUEL_PLACEMENT_LEFT, DRIVE_FORWARD
	}

	public Goal autoGoal;

	public enum State {
		INIT, WAIT_FOR_TIME, DONE, TURN, GEAR_PLACEMENT, VISION_ALIGNMENT, VISION_SECOND, MOVE_TO_LIFT, PLACE_GEAR, BACK_UP, DISPENSE_DA_FUEL
	}

	public State state;

	// Constructor
	public Drive(Joystick joystick, Gear gear, Fuel fuel) {
		oldEMA = 0;
		fuelClass = fuel;
		right1 = new CANTalon(RobotMap.RIGHT_ONE_DRIVE);
		right2 = new CANTalon(RobotMap.RIGHT_TWO_DRIVE);
		left1 = new CANTalon(RobotMap.LEFT_ONE_DRIVE);
		left2 = new CANTalon(RobotMap.LEFT_TWO_DRIVE);
		gearValue = .1;
		accel = new BuiltInAccelerometer();
		pressed = false;
		// creating a gear system
		joystick1 = joystick;
		// Create the RobotDrive object
		driveStyle = new RobotDrive(right1, left1);
		right1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		right1.enableBrakeMode(true);
		left1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		left1.enableBrakeMode(true);
		// Set left2 and right2 to follow the commands of left1 and left2
		right2.changeControlMode(TalonControlMode.Follower);
		right2.enableControl();
		right2.enableBrakeMode(true);
		right2.set(right1.getDeviceID());
		left2.changeControlMode(TalonControlMode.Follower);
		left2.enableControl();
		left2.enableBrakeMode(true);
		left2.set(left1.getDeviceID());
		autoGoal = Goal.DO_NOTHING;
		state = State.INIT;
		currentAngle = 0;
		speedByTime = -0.33;
		rotateValue = 0;

		try {
			ahrs = new AHRS(SPI.Port.kOnboardCS0);
		} catch (RuntimeException ex) {
			System.out.println("Error starting the Nav-X");
		}
		ahrs.zeroYaw();
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit(AutoChoices autoSelected) {

		ahrs.zeroYaw();

		// currentAngle = ahrs.getYaw();
		System.out.println("We are in AutoInit");
		right1.setPosition(0);
		left1.setPosition(0);

		switch (autoSelected) {

		case DO_NOTHING:
			System.out.println("Do Nothing");
			autoGoal = Goal.DO_NOTHING;
			break;

		case DRIVE_TO_DA_PIN:
			System.out.println("Drive to the pin");
			autoGoal = Goal.DRIVE_FORWARD;
			break;

		case BASE_LINE_TIME_SENSOR:
			System.out.println("Base Line only");
			autoGoal = Goal.BASE_LINE;
			turnAngle = largeAngle;
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

		case CENTER_GEAR_PLACEMENT:
			System.out.println("Center Gear Placement");
			turnAngle = 0;
			timeOutValueSecondMove = .6;
			autoGoal = Goal.TURN_GEAR_PLACEMENT;
			state = State.INIT;
			break;

		case VISION_GEAR_LEFT:
			System.out.println("vision left Gear Placement");
			turnAngle = 60;
			timeOutValueSecondMove = .6;
			autoGoal = Goal.VISION_GEAR_LEFT;
			state = State.INIT;
			break;

		case VISION_GEAR_RIGHT:
			System.out.println("vision right Gear Placement");
			turnAngle = -60;
			timeOutValueSecondMove = .6;
			autoGoal = Goal.VISION_GEAR_RIGHT;
			state = State.INIT;
			break;

		case GEAR_PLACEMENT_DREAM:
			System.out.println("Gear Placement Dream");
			turnAngle = -0;
			timeOutValueSecondMove = .6;
			autoGoal = Goal.GEAR_PLACEMENT_DREAM;
			state = State.INIT;
			break;
		case GEAR_AND_FUEL_PLACEMENT_LEFT:
			System.out.println("Gear and Fuel Placement Left");
			// turnAngle = 60;
			timeOutValueSecondMove = 8;
			autoGoal = Goal.GEAR_AND_FUEL_PLACEMENT_LEFT;
			state = State.INIT;
			break;
		default:

		}
	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic(Gear gear) {
		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		// SmartDashboard.putNumber("SONAR", sonar.getValue());
		SmartDashboard.putNumber("RIGHT ENCODER COUNT", right1.getPosition());
		SmartDashboard.putNumber("LEFT ENCODER COUNT", left1.getPosition());

		switch (autoGoal) {
		case DO_NOTHING:
			break;
		case DRIVE_FORWARD:
			// Subtract 5 because of a slight coast
			if (Math.abs(left1.getPosition()) >= (120 - 5) * ConstantMap.FAST_COUNTS_INCH) {
				right1.set(0);
				left1.set(0);
				System.out.println("Arrived at the gear pin");
			} else {
				chessyDriveAuto(-0.25, 0);
			}
			break;

		case BASE_LINE:
			if (state == State.INIT) {
				// ahrs.zeroYaw();
				state = State.WAIT_FOR_TIME;
				startTime = Timer.getFPGATimestamp();
				System.out.println("Start:");
				System.out.println(startTime);
				startTime += timeoutValue;
				gear.gearClawSet(-.4);

			} else if (state == State.WAIT_FOR_TIME) {

				if (/*
					 * Timer.getFPGATimestamp() >= (startTime + timeoutValue)||
					 * // 82 because of the 28 from the length of the bot from
					 * the 110 inches taht it has to travel // as well as the -5
					 * because of the slight coast
					 */Math.abs(left1.getPosition()) > (72 - 5) * ConstantMap.FAST_COUNTS_INCH) {
					right1.set(0);
					left1.set(0);
					state = State.DONE;
					startTime = Timer.getFPGATimestamp();
				} else {
					chessyDriveAuto(-0.33, 0);
				}

				// } else if (state == State.MOVE_TO_LIFT) {
				//
				// if (Timer.getFPGATimestamp() >= (startTime +
				// timeOutValueSecondMove))
				//
				// {
				// right1.set(0);
				// left1.set(0);
				// state = State.DONE;
				// startTime = Timer.getFPGATimestamp();
				// System.out.println("dun!");
				// System.out.println(Timer.getFPGATimestamp());
				// } else {
				// moveToLift(gear, visionAngle);
				// }

			}
			break;
		case GEAR_AND_FUEL_PLACEMENT_LEFT:

			if (state == State.INIT) {
				left1.setPosition(0);
				state = State.DISPENSE_DA_FUEL;
				startTime = Timer.getFPGATimestamp();
				startTime += 3.0;
				gear.gearClawSet(.4);
			} else if (state == State.DISPENSE_DA_FUEL) {
				if (Timer.getFPGATimestamp() >= startTime) {
					System.out.println("Move to Lift");
					fuelClass.fuelDischargeStop();
					state = State.MOVE_TO_LIFT;
					startTime = Timer.getFPGATimestamp();
				} else {
					fuelClass.dischargeFuel();
				}

			}

			// else if (state == State.WAIT_FOR_TIME)
			// {
			// if (Timer.getFPGATimestamp() >= (startTime + timeoutValue))
			// {
			// right1.set(0);
			// left1.set(0);
			// state = State.MOVE_TO_LIFT;
			// }
			// else
			// {
			// chessyDriveAuto(-0.33, 0);
			// }
			// }
			else if (state == State.MOVE_TO_LIFT) {
				if (Timer.getFPGATimestamp() >= (startTime + 6.0)) {
					System.out.println("Vision Alignment");
					right1.set(0);
					left1.set(0);
					state = State.PLACE_GEAR;
					startTime = Timer.getFPGATimestamp();
				} else if (Math.abs(left1.getPosition()) >= ((128 - 5) * ConstantMap.FAST_COUNTS_INCH)) {
					System.out.println("Encoder Stop");
					right1.set(0);
					left1.set(0);
					state = State.PLACE_GEAR;
				} else {
					chessyDriveAuto(-0.33, 0);
				}

			}
			// else if(state == State.VISION_ALIGNMENT ){
			// if (visionAlignment())
			// {
			// visionAngle = ahrs.getAngle();
			// state = State.VISION_SECOND;
			// startTime = Timer.getFPGATimestamp();
			// }
			//
			// }
			// else if (state == State.VISION_SECOND) {
			//
			// if (Timer.getFPGATimestamp() >= (startTime
			// + timeoutValueToLift / 2.0))
			// {
			// // 8// if (visionSecond()) {
			// visionAngle = ahrs.getAngle();
			// state = State.PLACE_GEAR;
			// startTime = Timer.getFPGATimestamp();
			// left1.setPosition(0);
			//
			// }
			//
			//
			// else {
			// driveStyle.arcadeDrive(0, 0, false);
			// System.out.println("PROBLEM");
			// }
			// }
			else if (state == State.PLACE_GEAR) {
				if (Timer.getFPGATimestamp() >= (startTime + timeOutValueSecondMove)) {
					right1.setPosition(0);
					left1.setPosition(0);
					placeGearAutoStop(gear);
					startTime = Timer.getFPGATimestamp();
					state = State.BACK_UP;

				}
				// else if (Timer.getFPGATimestamp() >= (startTime
				// + timeoutValueToLift / 4))
				// {
				// placeGearAuto(gear);
				// }
				else {
					placeGearAuto(gear);
				}
			} else if (state == State.BACK_UP) {
				if (Math.abs(left1.getPosition()) > (28 - 5) * ConstantMap.FAST_COUNTS_INCH) {
					right1.set(0);
					left1.set(0);
					state = State.DONE;
					System.out.println("dun!");
					System.out.println(Timer.getFPGATimestamp());
					ahrs.zeroYaw();
				} else {
					// chessyDriveAuto(-0.5, 0);
					moveAwayLift(visionAngle);
				}
			}
			break;

		case GEAR_PLACEMENT_DREAM:

			if (state == State.INIT) {
				// ahrs.zeroYaw();
				left1.setPosition(0);
				state = State.WAIT_FOR_TIME;
				startTime = Timer.getFPGATimestamp();
				System.out.println("Start:");
				System.out.println(startTime);
				startTime += timeoutValue;
				gear.gearClawSet(-.4);

			} else if (state == State.WAIT_FOR_TIME) {

				if (Timer.getFPGATimestamp() >= (startTime + timeoutValue) * 3
						|| Math.abs(left1.getPosition()) > (82 - 5) * ConstantMap.FAST_COUNTS_INCH) {
					System.out.println(left1.getPosition());
					right1.set(0);
					left1.set(0);
					state = State.PLACE_GEAR;
					startTime = Timer.getFPGATimestamp();
					System.out.println(Timer.getFPGATimestamp());
				} else {
					chessyDriveAuto(-0.33, 0);
				}

			}

			// else if (state == State.MOVE_TO_LIFT) {
			//
			// if (Timer.getFPGATimestamp() >= (startTime +
			// timeOutValueSecondMove))
			//
			// {
			// right1.set(0);
			// left1.set(0);
			// state = State.PLACE_GEAR;
			// startTime = Timer.getFPGATimestamp();
			// System.out.println("dun!");
			// System.out.println(Timer.getFPGATimestamp());
			// } else {
			// moveToLift(gear, visionAngle);
			// }
			// }
			else if (state == State.PLACE_GEAR) {
				if (Timer.getFPGATimestamp() >= (startTime + timeOutValueSecondMove * 2)) {
					right1.setPosition(0);
					left1.setPosition(0);
					startTime = Timer.getFPGATimestamp();
					state = State.BACK_UP;
					System.out.println("Backing up");
				} else {
					placeGearAuto(gear);
				}

			} else if (state == State.BACK_UP) {
				if (Math.abs(left1.getPosition()) > (28 - 5) * ConstantMap.FAST_COUNTS_INCH) {
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
			break;
		default:
		}
	}

	public void teleopPeriodic() {

		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		SmartDashboard.putNumber("LEFT ENCODER COUNT", left1.getPosition());
		SmartDashboard.putNumber("RIGHT ENCODER COUNT", right1.getPosition());
		SmartDashboard.putNumber("X", accel.getX());
		SmartDashboard.putNumber("Y", accel.getY());
		SmartDashboard.putNumber("Z", accel.getZ());
		SmartDashboard.putNumber("Speed Left", left1.getSpeed());
		SmartDashboard.putNumber("Speed Right", right1.getSpeed());
		speedIncreaseXbox = joystick1.getRawAxis(3);
		// System.out.println(speedIncreaseXbox);
		speedDecreaseXbox = joystick1.getRawAxis(2);

		if (gearValue < 0.4) {
			gearValue = .4;

		} else if (gearValue > 1) {

			gearValue = 1;

		} else if (speedIncreaseXbox > 0.8 && !pressed) {
			System.out.println("Speed Increase");
			gearValue += speedIncrease;
			pressed = true;

		} else if (joystick1.getRawAxis(2) > .8 && !pressed) {

			gearValue -= speedDecrease;
			pressed = true;

		} else if (!(joystick1.getRawAxis(3) > .8 || joystick1.getRawAxis(2) > .8))

		{
			pressed = false;
		}

		if (joystick1.getRawButton(9)) {
			ahrs.zeroYaw();
		}

		chessyDrive(joystick1, 1, joystick1, 0);

	}

	public void testPeriodic() {

	}

	public Joystick getJoystick() {
		return joystick1;

	}

	private void encoderStop() {
		System.out.println(right1.getPosition());
		System.out.println(left1.getPosition());
		right1.set(0);
		left1.set(0);
	}

	public void chessyDrive(Joystick joys1, int axis1, Joystick joys2, int axis2) {

		SmartDashboard.putNumber("ANGLE NAVX", ahrs.getAngle());
		double moveValue = (joys1.getRawAxis(1) * gearValue);
		double rotateValue = (joys1.getRawAxis(4) * -1) * gearValue;

		if (rotateValue < 0.1 && rotateValue > -0.1 && counter > 20) {

			if (ahrs.getAngle() > 3 + currentAngle) {

				rotateValue += .15;

			} else if (ahrs.getAngle() < -3 + currentAngle) {

				rotateValue -= .15;

			}

		} else if (counter <= 20 && rotateValue < 0.1 && rotateValue > -0.1) {

			currentAngle = ahrs.getAngle();
			counter++;

		} else {

			currentAngle = ahrs.getAngle();
			counter = 0;

		}

		moveValue = smoothMove(moveValue);

		driveStyle.arcadeDrive(moveValue, rotateValue, false);
	}

	public void chessyDriveAuto(double moveValue, double rotateValue) {

		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getYaw() > 1.2 + currentAngle) {

				rotateValue += rotateIncrease;

			} else if (ahrs.getYaw() < -1.2 + currentAngle) {

				rotateValue -= rotateDecrease;

			}

		}
		// moveValue = smoothMove(moveValue);

		driveStyle.arcadeDrive(moveValue, rotateValue, false);
	}

	private boolean turnAuto(int optimalAngle) {
		double rotateValue = 0;

		if (ahrs.getAngle() > optimalAngle + 3) {

			rotateValue += rotateIncrease;
		} else if (ahrs.getAngle() < optimalAngle - 3) {

			rotateValue -= rotateDecrease;

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

				rotateValue += rotateIncrease;

			} else if (ahrs.getAngle() < -1 + alignedAngle) {

				rotateValue -= rotateDecrease;

			}

		}

		// gear.gearArmSet(-.5);
		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private void placeGearAuto(Gear gear) {

		gear.gearClawSet(0.3);
		gear.gearArmSet(0.5);

	}

	private void placeGearAutoStop(Gear gear) {
		gear.gearClawSet(0);
		gear.gearArmSet(0);
	}

	private void moveGearUp(Gear gear) {

		gear.gearArmSet(0.3);

	}

	private void moveAwayLift(double alignedAngle) {

		double rotateValue = 0;
		double moveValue = +.32;

		if (rotateValue < 0.1 && rotateValue > -0.1) {

			if (ahrs.getAngle() > 1 + alignedAngle) {

				rotateValue += rotateIncrease;

			} else if (ahrs.getAngle() < -1 + alignedAngle) {

				rotateValue -= rotateDecrease;
			}

		}

		driveStyle.arcadeDrive(moveValue, rotateValue, false);

	}

	private boolean visionAlignment() {

		if (SmartDashboard.getNumber("CenterX") > 185) {

			driveStyle.arcadeDrive(0, 0.28, false);

		} else if (SmartDashboard.getNumber("CenterX") < 135) {

			driveStyle.arcadeDrive(0, -0.28, false);

		} else {

			return true;
		}

		return false;
	}

	private boolean visionSecond() {

		if (SmartDashboard.getNumber("CenterX") > 170) {

			driveStyle.arcadeDrive(0, 0.26, false);

		} else if (SmartDashboard.getNumber("CenterX") < 150) {

			driveStyle.arcadeDrive(0, -0.26, false);

		} else {

			return true;
		}

		return false;
	}

	private void timeStamps() {
		System.out.println("Start:");
		System.out.println(startTime);
		startTime += testBotTimeoutValueToLift;
		System.out.println("end:");
		System.out.println(startTime);
	}

	private void stateWaitForTime() {
		if (Timer.getFPGATimestamp() >= (startTime + testBotTimeoutValue))

		{
			right1.set(0);
			left1.set(0);
			state = State.TURN;
			startTime = Timer.getFPGATimestamp();
			System.out.println(Timer.getFPGATimestamp());
		} else if (right1.getPosition() >= testBotRightEncoder || left1.getPosition() <= testBotLeftEncoder) {
			encoderStop();
			state = State.TURN;

		} else {
			chessyDriveAuto(-0.33, 0);
		}
	}

	private void moveToLift() {
		if (Timer.getFPGATimestamp() >= (startTime + testBotTimeoutValueToLift)) {
			right1.set(0);
			left1.set(0);
			startTime = Timer.getFPGATimestamp();
			System.out.println(Timer.getFPGATimestamp());
		}
		if (right1.getPosition() >= testBotRightMoveToLiftEncoder
				|| left1.getPosition() <= testBotLeftMoveToLiftEncoder) {
			encoderStop();
			placeGearAuto(gear);
			moveGearUp(gear);
			state = State.DONE;
		} else {
			chessyDriveAuto(speedByTime, rotateValue);
		}
	}
	// private void changeDriveStyle() {
	// newButtonValue = joystick1.getRawButton(RobotMap.BUTTON);
	//
	// if (newButtonValue != oldButtonValue) {
	// if (newButtonValue == true) {
	// if (driveType == false) {
	// // driveStyle.arcadeDrive(joystick2, 1, joystick1, 0);
	// driveType = true;
	// } else {
	// // driveStyle.tankDrive(joystick1, joystick2);
	// driveType = false;
	// }
	//
	// }
	// oldButtonValue = newButtonValue;
	// }
	// }

	private double smoothMove(double _value) {
		double value = _value;
		if ((value > 0) && (oldEMA < -xSpeedDeadBand)) // || ((value < 0) &&
														// (oldEMA > 0))){
		{
			// we're tipping!!
			value = 0;
			timePeriodSF = kHighSmooth;
			System.out.println("Tipping forward");
		} else if ((value < 0) && (oldEMA > xSpeedDeadBand)) {// we're tipping!!
			value = 0;
			timePeriodSF = kHighSmooth;
			System.out.println("tipping backward");
		}

		double smoothFactor = 2.0 / (timePeriodSF + 1);
		value = oldEMA + smoothFactor * (value - oldEMA);

		if (Math.abs(oldEMA) < xSpeedDeadBand) {
			timePeriodSF = kLowSmooth;
		}

		oldEMA = value;
		SmartDashboard.putNumber("smooth", value);
		return value;
	}

}
