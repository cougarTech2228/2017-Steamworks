package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;

public class Climb {
	private CANTalon climberMotor;
	private VictorSP climber;
	private boolean newValue = false;
	private boolean oldValue = false;
	private boolean climberOn = false;
	private Joystick joystick;

	// Constructor
	public Climb(Joystick joy) {

		joystick = joy;
		climberMotor = new CANTalon(/*RobotMap.ROBOT_CLIMBER*/7);
//		climber = new VictorSP(1);

	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit() {

	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic() {

	}

	// Called continuously during the teleop period
	public void teleopPeriodic() {
		// If button 9 is pressed, then the code will look for the motor's speed
		// percentage
		// If percentage is 100, then it will make it 0
		// If percentage is 0, then it will make it 100

		newValue = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_11_CLIMB_ON_AND_OFF*/8);

		if (newValue != oldValue) {
			
			if (newValue) {
				
				if (!climberOn) {
					
					climberMotor.set(-1);
					climberOn = true;
					
				}else {
					
					climberMotor.set(0);
					climberOn = false;
					
				}

			}
			oldValue = newValue;
		}
		
	}

	// Called continuously during testing
	public void testPeriodic() {

	}
}