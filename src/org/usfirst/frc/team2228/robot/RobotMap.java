package org.usfirst.frc.team2228.robot;

public final class RobotMap {
	// Joysticks
	public static int RIGHT_SIDE_JOYSTICK_ONE = 1;
	public static int LEFT_SIDE_JOYSTICK_ONE = 1;


// Gear
	// Motor ID's
		public static int GEAR_JAW = 5;
		public static int GEAR_ARM = 6;
		public static int GEAR_LOAD_STATION_GUIDE = 9;

		// Button 3 releases the gear
		public static int BUTTON_3_RELEASE_THE_GEAR = 3;
		// Button 2 collects the gear
		public static int BUTTON_2_COLLECT_THE_GEAR = 2;
		// Button 4 moves the arm down
		public static int BUTTON_5_MOVE_ARM_DOWN = 5;
		// Button 1 moves the arm up
		public static int BUTTON_6_MOVE_ARM_UP = 6;
		// Button 6 lets the robot collect the gear from the load station
		public static int BUTTON_1_GEAR_COLLECTION = 1;
		
// Fuel
	//  Motor IDs
		public static int FUEL_FURNACE_ROLLER_MOTOR = 6;
		public static int FUEL_LOAD_STATION_ROLLER_MOTOR = 5;
		public static int FUEL_CONVEYOR_ROLLER_MOTOR = 7; 
		public static int FUEL_LOAD_STATION_GUIDE_MOTOR = 8;
		public static int FUEL_LOAD_STATION_MOTOR = 4;
	// Button IDs

		// Button 5 collects the fuel from the load station
		public static int BUTTON_4_LOAD_STATION_COLLECTOR = 4;
		// Button 7 dispenses the fuel into the furnace
		public static int BUTTON_7_FURNACE_ROLLER_DISPENSER = 7;
		// Button 10 (clicking the right stick) turns the floor fuel collector on and off
		public static int BUTTON_10_FLOOR_FUEL_COLLECTOR = 10;

// Drive
	// CANTalon ID's
		public static int RIGHT_ONE_DRIVE = 1;														
		public static int RIGHT_TWO_DRIVE = 2;
		public static int LEFT_ONE_DRIVE = 3;
		public static int LEFT_TWO_DRIVE = 4;
	// Button ID's
		// button 8 on joystick 2 increases the speed of the robot
		public static int JOY1_BUTTON_9_INCREASE_SPEED = 9;
		// button 7 on joystick 2 decreases the speed of the robot
		public static int JOY1_BUTTON_10_DECREASE_SPEED = 10;

		
// Climb
	// Motor ID
		public static int ROBOT_CLIMBER = 7;
		public static int ROBOT_CLIMBER_TEST = 1;
	// Button ID

	public static int BUTTON_8_CLIMB_ON_AND_OFF = 8;
	

}
