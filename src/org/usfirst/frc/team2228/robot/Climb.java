package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

public class Climb
{
	private CANTalon climber;
	private boolean newValue = false;
	private boolean oldValue = false;
	private Joystick joystick;

	// Constructor
	public Climb(Joystick joy)
	{

		joystick = joy;
		climber = new CANTalon(RobotMap.ROBOT_CLIMBER);

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
		// If button 8 is pressed, then the code will look for the motor's speed
		// percentage
		// If percentage is 100, then it will make it 0
		// If percentage is 0, then it will make it 100
		newValue = joystick.getRawButton(9);

		if (newValue != oldValue)
		{
			if (newValue == true)
			{
				climber.set(1);
			}
			else if (climber.get() == 1)
			{
				climber.set(0);
			}
			oldValue = newValue;
		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}