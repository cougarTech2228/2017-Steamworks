package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gear
{
	private CANTalon gearArm;
	private CANTalon gearJaw;
	private boolean loadingActivated;
	private Spark guide;
	private Servo servo1;
	private Servo servo2;
	private double servoAngle1;
	private double minServoAngle1;
	private double maxServoAngle1;
	private double servoAngle2;
	private double minServoAngle2;
	private double maxServoAngle2;
	private boolean firstGearGuideCollectionValue = false;
	private boolean lastGearGuideCollectionValue = false;
	private boolean guideOpen;
	private boolean gearCollection = false;
	private boolean moveGearArmUp = false;
	private boolean moveGearArmDown = false;
	private boolean gearGraspRelease = false;
	private boolean gearGuideCollect = false;
	private double open;
	private double guideUp = 0.4;
	private double guideDown = -0.4;
	private Joystick joystick;
	private double gearArmDownXbox;
	private double gearArmUpXbox;
	private boolean dropDaGear = false;
	private double competitionArmUp = 1;
	private double competitionArmDown = 1;
	private double competitionGearCollectionValue = 1;
	private double competitionGearReleaseValue = 1;
	private double competitionDropArm = 1;
	private double competitionDropRelease = 1;
	private double gearJawOpenValue = -0.3;
	private double gearJawCloseValue = 0.3;
	private double armUp = -0.7;
	private double armDown = 0.4;

	private double gearCollectionValue = -0.4;
	private double gearReleaseValue = 0.4;
	private double rollerVelocity = -0.5;
	private AnalogInput potentiometerArm;
	private AnalogInput potentiometerJaw;
	private DigitalInput fwdLimitSwitch;
	private DigitalInput revLimitSwitch;
	private int potentiometerMax = 0;
	// private DigitalInput gearDetector;

	// Constructor
	public Gear(Joystick joy)
	{
		servo1 = new Servo(0);
		servo2 = new Servo(1);
		minServoAngle1 = 0;
		maxServoAngle1 = 180;
		minServoAngle2 = 0;
		maxServoAngle2 = 180;
		guide = new Spark(RobotMap.GEAR_LOAD_STATION_GUIDE);
		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		gearJaw = new CANTalon(RobotMap.GEAR_JAW);

		potentiometerArm = new AnalogInput(0);
		potentiometerJaw = new AnalogInput(1);
		SmartDashboard.putNumber("ArmPotentiometer",
				potentiometerArm.getValue());
		SmartDashboard.putNumber("JawPotentiometer",
				potentiometerJaw.getValue());

		fwdLimitSwitch = new DigitalInput(6);
		revLimitSwitch = new DigitalInput(7);
		// gearDetector = new DigitalInput(5);
		loadingActivated = false;
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit()
	{

	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic()
	{

	}

	// Called continuously during the teleop period
	public void teleopPeriodic(Fuel fuel)
	{
		gearCollection = joystick
				.getRawButton(RobotMap.BUTTON_2_COLLECT_THE_GEAR);
		gearGraspRelease = joystick
				.getRawButton(RobotMap.BUTTON_3_RELEASE_THE_GEAR);
		if (gearCollection)
		{
			gearJaw.set(gearCollectionValue);
		}
		else if (gearGraspRelease)
		{
			gearJaw.set(gearReleaseValue);
		}
		else
		{
			gearJaw.set(0);
		}

		moveGearArmUp = joystick.getRawButton(RobotMap.BUTTON_6_MOVE_ARM_UP);
		moveGearArmDown = joystick
				.getRawButton(RobotMap.BUTTON_5_MOVE_ARM_DOWN);

		if (moveGearArmUp)
		{
			if (gearArm.isRevLimitSwitchClosed())
			{
				// gearArm.set(0);
			}
			else
			{
				gearArm.set(armUp);
			}
			// gearJaw.set(gearCollectionValue);
		}
		else if (moveGearArmDown)
		{
			gearArm.set(armDown);
			// gearJaw.set(gearCollectionValue);
		}
		else
		{
			gearArm.set(0);
			// leftGearCollector.set(0);
			// gearCollector.set(0);
		} //

		gearGuideCollect = joystick
				.getRawButton(RobotMap.BUTTON_1_GEAR_COLLECTION);

		if (gearGuideCollect)
		{
			if (servoAngle1 < maxServoAngle1)
			{
				servo1.set(maxServoAngle1);
				;
			}
			if (servoAngle2 > minServoAngle2)
			{
				servo1.set(minServoAngle2);
				;
			}
			servo1.set(servoAngle1);
			servo2.set(servoAngle2);
			if (revLimitSwitch.get())
			{
				guide.set(guideDown);

			}
			else
			{
				guide.set(0);
			}

			fuel.fuelLoadStationRollerSet(rollerVelocity);
			if (/* gearArm.isFwdLimitSwitchClosed() */ potentiometerArm
					.getValue() >= 500)
			{
				gearJaw.set(gearJawOpenValue);
				gearArmSet(0);
			}
			else
			{
				gearArm.set(armUp);
				gearJaw.set(gearJawCloseValue * 2);
			}

		}
		else
		{
			if (servoAngle1 > minServoAngle1)
			{
				servo1.set(minServoAngle1);
			}
			if (servoAngle2 < maxServoAngle2)
			{
				servo2.set(maxServoAngle2);
			}
			if (fwdLimitSwitch.get())
			{
				guide.set(guideUp);
				fuel.fuelLoadStationRollerSet(0);

			}
			else
			{
				guide.set(0);
			}
			loadingActivated = false;

		}

		// if (firstGearGuideCollectionValue != lastGearGuideCollectionValue) {
		// if(firstGearGuideCollectionValue){
		// if(!guideOpen){
		// gearLoadCollectionGuide.set(gearCollectionValue)
		// gearArm.set(armUp)
		// }
		// else{
		// `}
		// }
		// }

		SmartDashboard.putNumber("Pot1", potentiometerArm.getValue());
		// SmartDashboard.putNumber("Pot2", potentiometerJaw.getValue());
		// SmartDashboard.putBoolean("da gear is dere", gearDetector.get());

	}

	public boolean loadingGear()
	{

		return joystick.getRawButton(6);
	}

	public void gearClawSet(double vel)
	{
		gearJaw.set(vel);
	}

	public void gearArmSet(double vel)
	{
		if (vel > 0 && potentiometerArm.getValue() > potentiometerMax)
		{
			gearArm.set(vel);
		}
		else if (vel < 0)
		{
			gearArm.set(vel);
		}
		// gearJaws.set(vel);
	}

	public void raiseTheArm()
	{
		if (gearArm.isFwdLimitSwitchClosed()
				&& potentiometerArm.getValue() > potentiometerMax)
		{
			gearArmSet(armUp);

		}
		else if (!gearArm.isRevLimitSwitchClosed()
				&& !gearArm.isFwdLimitSwitchClosed())
		{
			gearArm.set(armUp);
		}
		else
		{

		}
	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}
//
