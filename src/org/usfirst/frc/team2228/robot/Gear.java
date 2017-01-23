package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gear
{
	private CANTalon gearArm;
	private CANTalon leftGearCollector;
	private CANTalon rightGearCollector;
	private boolean gearCollection = false;
	private boolean gearNothing = false;
	private boolean moveGearArm = false;
	private boolean moveGearArmDown = false;
	private Joystick joystick;

	// Constructor
	public Gear(Joystick joy)
	{

		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		leftGearCollector = new CANTalon(RobotMap.LEFT_GEAR_COLLECTOR);
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

		gearCollection = joystick.getRawButton(5);
		if (gearCollection)
		{
			leftGearCollector.set(-0.9);
			rightGearCollector.set(0.9);
		}else if(joystick.getRawButton(3)){
			leftGearCollector.set(.9);
			rightGearCollector.set(-.9);
		}
		else
		{
			leftGearCollector.set(0);
			rightGearCollector.set(0);
		}

		moveGearArm = joystick.getRawButton(6);
		moveGearArmDown = joystick.getRawButton(4);
		if (moveGearArm)
		{
			gearArm.set(1);
//			leftGearCollector.set(1);
//			rightGearCollector.set(1);
		}
		else if (moveGearArmDown)
		{
		gearArm.set(-0.5);	
		}
		else
		{
			gearArm.set(0);
//			leftGearCollector.set(0);
//			rightGearCollector.set(0);
		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}