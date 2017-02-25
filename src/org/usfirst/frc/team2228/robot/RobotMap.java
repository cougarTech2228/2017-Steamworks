package org.usfirst.frc.team2228.robot;

public final class RobotMap
{
// Joysticks
		public static int RIGHT_SIDE_JOYSTICK_ONE = 0;
		public static int LEFT_SIDE_JOYSTICK_ONE = 1;

// Gear
	// Motor ID's
		public static int GEAR_JAW = 5;
		public static int GEAR_ARM = 6;
		public static int GEAR_LOAD_STATION_GUIDE = 9; //Spark 1
	// Button ID's
		// Button 3 releases the gear
		public static int BUTTON_3_RELEASE_THE_GEAR = 3;
		// Button 2 collects the gear
		public static int BUTTON_2_COLLECT_THE_GEAR = 2;
		// Button 4 moves the arm down
		public static int BUTTON_1_MOVE_ARM_DOWN = 1;
		// Button 1 moves the arm up
		public static int BUTTON_4_MOVE_ARM_UP = 4;
		// Button 6 lets the robot collect the gear from the load station
		public static int BUTTON_6_GEAR_COLLECTION = 6;
		
// Fuel
	//  Motor IDs
		public static int FUEL_FURNACE_ROLLER_MOTOR = 6; //VictorSP 2
		public static int FUEL_LOAD_STATION_ROLLER_MOTOR = 5; //VictorSP 3
		public static int FUEL_CONVEYOR_ROLLER_MOTOR = 7; //VictorSP 1
		public static int FUEL_LOAD_STATION_GUIDE_MOTOR = 8; //Spark 2
	// Button IDs
		// Button 5 collects the fuel from the load station
		public static int BUTTON_5_LOAD_STATION_COLLECTOR = 5;
		// Button 7 dispenses the fuel into the furnace
		public static int BUTTON_7_FURNACE_ROLLER_DISPENSER = 7;


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
		public static int BUTTON_8_CLIMB_ON_AND_OFF = 8;
}
