package org.usfirst.frc.team2228.robot;

//Carrying over the classes from other libraries
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Enumeration;

public class Drive
{
	//Names of the classes
	private RobotDrive joydrive;
	private Joystick joy1;
	private Joystick joy2;
	private CANTalon right1;
	private CANTalon left1;
	private CANTalon right2;
	private CANTalon left2;
	private RobotMap map;
	private ConstantMap constant;
	private boolean boo = false;
	private boolean boothe2 = false;
	public enum Goal{
		DO_NOTHING,
		FORWARD_BY_TIME,
		
	}
	
	//Constructor
	public Drive(){
		//"Creating" the objects
		//Create the four motor controller objects for the drive base
		right1 = new CANTalon(RobotMap.RIGHT_ONE_DRIVE); //2
	    right2 = new CANTalon(RobotMap.RIGHT_TWO_DRIVE); //1
	    left1 = new CANTalon(RobotMap.LEFT_ONE_DRIVE); //3
	    left2 = new CANTalon(RobotMap.LEFT_TWO_DRIVE); //4
	    //Create the RobotDrive object
	    joydrive = new RobotDrive(right1, left1);
	    //Set left2 and right2 to follow the commands of left1 and left2
	    right2.changeControlMode(TalonControlMode.Follower);
	    right2.enableControl();
	    right2.set(right1.getDeviceID());
	    left2.changeControlMode(TalonControlMode.Follower);
	    left2.enableControl();
	    left2.set(left1.getDeviceID());
	    //Creating the joystick objects
	    joy1 = new Joystick(0);
	    joy2 = new Joystick(1);
		
	}
	//Called once at the beginning of the autonomous period
	public void autonomousInit(String autoSelected) {
		switch (autoSelected) {
			
			case ConstantMap.doNothing:
		
				// Put custom auto code here
				break;
			default:
				// Put default auto code here
				break;
		}
		}
	//Called continuously during the autonomous period
	public void autonomousPeriodic() {
		
	}
	
	//Called continuously during the teleop period
	public void teleopPeriodic() {
		//Use two joysticks to drive
		//Press a button to enter "chessyDrive" otherwise drive in "tankDrive"
		boo = joy2.getRawButton(7);
		if (boothe2 = true){
			
			joydrive.tankDrive(joy1, joy2);
			}
		else{
			joydrive.chessyDrive(joy1, joy2);
		}
		
		//Press a button and receive an action 
		//For example, press button 10, and the motors move at a value of one
		if(joy2.getRawButton(10)){
			left1.set(1);
		}
		if(joy1.getRawButton(10)){
			right1.set(-1);
		}
	}
	//Called continuously during testing
	public void testPeriodic() {
		
	}
	
	public Joystick getJoystick(){
		return joy1;
	}
}