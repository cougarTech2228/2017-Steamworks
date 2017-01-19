package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

public class Climb
{
	private CANTalon climber;
	private boolean climboo = false;
	private boolean climboo2 = false;
	private Joystick joystick;

	// Constructor
	public Climb(Joystick joy)
	{

		joystick = joy;
		climber = new CANTalon(RobotMap.RIGHT_SHOOTER_ONE);

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
		climboo = joystick.getRawButton(8);
		if (climboo != climboo2)
		{
			climboo2 = climboo;

			if (climber.get() == 1)
			{
				climber.set(0);
			}
			else
			{
				if (climber.get() == 0)
				{
					climber.set(1);

				}
			}
		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}