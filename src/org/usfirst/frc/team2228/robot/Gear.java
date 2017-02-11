package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gear
{
	private CANTalon gearArm;
	private CANTalon rightGearCollector;
	private boolean gearCollection = false;
	private boolean moveGearArmUp = false;
	private boolean moveGearArmDown = false;
	private Joystick joystick;
	private boolean dropDaGear = false;

	// Constructor
	public Gear(Joystick joy)
	{

		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		// leftGearCollector = new CANTalon(RobotMap.LEFT_GEAR_COLLECTOR);
		rightGearCollector = new CANTalon(RobotMap.RIGHT_GEAR_COLLECTOR);

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
				.getRawButton(RobotMap.JOY1_BUTTON_4_COLLECT_THE_GEAR);
		if (gearCollection)
		{
			// leftGearCollector.set(-0.9);

			rightGearCollector.set(1);

		}
		else if (joystick.getRawButton(RobotMap.JOY1_BUTTON_3_RELEASE_THE_GEAR))
		{
			// leftGearCollector.set(.9);

			rightGearCollector.set(-1);

		}
		else
		{
			// leftGearCollector.set(0);
			rightGearCollector.set(0);
		}


		moveGearArmUp = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_6_MOVE_ARM_UP);
		moveGearArmDown = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_5_MOVE_ARM_DOWN);
		if (moveGearArmUp)

		{
			gearArm.set(1);
			// leftGearCollector.set(1);
			rightGearCollector.set(1);
		}
		else if (moveGearArmDown)
		{
			gearArm.set(-1);
		}
		else
		{
			gearArm.set(0);
			// leftGearCollector.set(0);
			rightGearCollector.set(0);
		}
		dropDaGear = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_2_DROP_THE_GEAR);
		if (dropDaGear)
		{

			gearArm.set(-1);
			rightGearCollector.set(1);

		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}
//