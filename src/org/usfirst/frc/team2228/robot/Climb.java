package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;

public class Climb
{
	private CANTalon climberMotor;
	private boolean newValue = false;
	private boolean oldValue = false;
	private boolean climberOn = false;
	private double maxClimberCurrent = 23;
	private double currentClimberCurrent;
	private PowerDistributionPanel pdp;
	private int fullPower = -1;
	private int noPower = 0;
	private Joystick joystick;

	// Constructor
	public Climb(Joystick joy, PowerDistributionPanel _pdp)
	{

		joystick = joy;
		climberMotor = new CANTalon(RobotMap.ROBOT_CLIMBER);
		pdp = _pdp;

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
		currentClimberCurrent = pdp.getCurrent(8);
		SmartDashboard.putNumber("Current to the Climber",currentClimberCurrent);
		newValue = joystick.getRawButton(RobotMap.BUTTON_8_CLIMB_ON_AND_OFF);
		
		if (maxClimberCurrent <= currentClimberCurrent)
		{
			climberMotor.set(.75);
			System.out.println("Current is too high, climber is recieving less power!");
		}
		else if (newValue != oldValue)
		{

			if (newValue)
			{

				if (!climberOn)
				{

					climberMotor.set(fullPower);
					climberOn = true;

				}
				else
				{
					climberMotor.set(noPower);
					climberOn = false;

				}

			}
			oldValue = newValue;
		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{

	}
}
