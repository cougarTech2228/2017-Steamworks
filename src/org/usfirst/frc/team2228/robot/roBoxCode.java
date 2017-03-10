package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class roBoxCode {
private CANTalon motor;
private DigitalInput up;
private DigitalInput down;



public roBoxCode(Joystick joy){
	up = new DigitalInput(0);
	down = new DigitalInput(1);
	motor = new CANTalon(2);
	
}
public void teleopPeriodic(){
	if(up.get() == false){
		
	}
}
public void testPeriodic(){
	
	
	
	}
}




