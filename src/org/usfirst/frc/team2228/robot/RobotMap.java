package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public final class RobotMap
{
// Joysticks
		
		
		public static XboxController xbox;
		public static Hand leftHand;
		public static Hand rightHand;
		public static double LEFT_SIDE_JOYSTICK_ONE = xbox.getX(leftHand);
		public static double RIGHT_SIDE_JOYSTICK_ONE = xbox.getY(rightHand);
//buttons
//		public static boolean BUTTON_ONE = xbox.getAButton();
//		public static boolean BUTTON_TWO = xbox.getBButton();
//		public static boolean BUTTON_THREE = xbox.getXButton();
//		public static boolean BUTTON_FOUR = xbox.getYButton();
//		public static boolean BUTTON_FIVE = xbox.getBumper(leftHand);
//		public static boolean BUTTON_SIX = xbox.getBumper(rightHand);
//		public static boolean BUTTON_SEVEN = xbox.getBackButton();
//		public static boolean BUTTON_EIGHT = xbox.getStartButton();
//		public static boolean BUTTON_NINE = xbox.getStickButton(leftHand);
//		public static boolean BUTTON_TEN = xbox.getStickButton(rightHand);
	
// Gear
	// Motor ID's
		public static int TEST_GEAR_ALERON = 2;
		public static int GEAR_JAW = 5;
		public static int GEAR_ARM = 6;
		public static int GEAR_LOAD_STATION_GUIDE = 9; //Spark 1
	// Button ID's
		// Button 3 releases the gear
		public static boolean BUTTON_THREE = xbox.getXButton();		
		// Button 2 collects the gear
		public static boolean BUTTON_TWO = xbox.getBButton();
		// Button 4 moves the arm down
		public static boolean BUTTON_ONE = xbox.getAButton();
		// Button 1 moves the arm up
		public static boolean BUTTON_FOUR = xbox.getYButton();
		// Button 6 lets the robot collect the gear from the load station
		public static boolean BUTTON_SIX = xbox.getBumper(rightHand);
		
// Fuel
	//  Motor IDs
		public static int FUEL_FURNACE_ROLLER_MOTOR = 6; //VictorSP 2
		public static int FUEL_LOAD_STATION_ROLLER_MOTOR = 5; //VictorSP 3
		public static int FUEL_CONVEYOR_ROLLER_MOTOR = 7; //VictorSP 1
		public static int FUEL_LOAD_STATION_GUIDE_MOTOR = 8; //Spark 2
	// Button IDs
		// Button 5 collects the fuel from the load station
		public static boolean BUTTON_FIVE = xbox.getBumper(leftHand);
		// Button 7 dispenses the fuel into the furnace
		public static boolean BUTTON_SEVEN = xbox.getBackButton();


// Drivebase
	// Motor ID's
		public static int RIGHT_ONE_DRIVE = 1;
		public static int RIGHT_TWO_DRIVE = 2;
		public static int LEFT_ONE_DRIVE = 3;
		public static int LEFT_TWO_DRIVE = 4;
		
// Climb
	// Motor ID
		public static int ROBOT_CLIMBER = 7;
		public static int ROBOT_CLIMBER_TEST = 1; //VictorSP
	// Button ID
		// button 8 on joystick 2 turns the climb motor on and off
		public static boolean BUTTON_EIGHT = xbox.getStartButton();			
}
