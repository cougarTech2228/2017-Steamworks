package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gear
{
	private CANTalon gearArm;
	private CANTalon gearJaw;
	private boolean gearCollection = false;
	private boolean moveGearArmUp = false;
	private boolean moveGearArmDown = false;
	private boolean gearGraspRelease = false;
	private Joystick joystick;
	private boolean dropDaGear = false;
	private double armUp = -0.7;
	private double armDown = 0.2;
	private double gearCollectionValue = 0.4;
	private double gearReleaseValue = -0.4;
	private double dropArm = -0.2;
	private double dropRelease = 0.5;

	// Constructor
	public Gear(Joystick joy)
	{

		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		// leftGearCollector = new CANTalon(RobotMap.LEFT_GEAR_COLLECTOR);
		gearJaw = new CANTalon(RobotMap.GEAR_JAW);

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
	public void teleopPeriodic()
	{

		gearCollection = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_3_COLLECT_THE_GEAR);
		gearGraspRelease = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_2_RELEASE_THE_GEAR);
		if (gearCollection)
		{
			// leftGearCollector.set(-0.9);

			gearJaw.set(gearCollectionValue);
		}
		else if (gearGraspRelease)
		{
			// leftGearCollector.set(.9);
			gearJaw.set(gearReleaseValue);
		}
		else
		{
			gearJaw.set(0);
		}
		// else
		// {
		// // leftGearCollector.set(0);
		// gearCollector.set(0);
		// }

		moveGearArmUp = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_6_MOVE_ARM_UP);
		moveGearArmDown = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_5_MOVE_ARM_DOWN);
		if (moveGearArmUp)
		{
			gearArm.set(armUp);
			// leftGearCollector.set(1);
			// rightGearCollector.set(1);
		}
		else if (moveGearArmDown)
		{
			gearArm.set(armDown);
		}
		else
		{
			gearArm.set(0);
			// leftGearCollector.set(0);
			// gearCollector.set(0);
		}
		// dropDaGear = joystick
		// .getRawButton(RobotMap.JOY1_BUTTON_1_DROP_THE_GEAR);
		// if (dropDaGear)
		// {
		// gearArm.set(dropArm);
		// gearCollector.set(dropRelease);
		// }

	}

	public void gearClawSet(double vel){
		gearJaw.set(vel);
	}
	
	public void gearArmSet(double vel){
		gearArm.set(vel);
	}
	
	// Called continuously during testing
	public void testPeriodic()
	{

	}
}
//