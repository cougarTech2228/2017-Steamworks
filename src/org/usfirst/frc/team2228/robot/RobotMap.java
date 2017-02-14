package org.usfirst.frc.team2228.robot;

public final class RobotMap
{
// Joysticks
		public static int RIGHT_SIDE_JOYSTICK_ONE = 0;
		public static int LEFT_SIDE_JOYSTICK_ONE = 1;

// Gear
	// CANTalon ID's
		public static int GEAR_JAW = 7;
		public static int GEAR_ARM = 5;
	// Button ID's
		// button 2 lowers the arm and releases the gear at the same time
		public static int JOY1_BUTTON_1_DROP_THE_GEAR = 1;
		// button 3 releases the gear
		public static int JOY1_BUTTON_2_RELEASE_THE_GEAR = 2;
		// button 4 collects the gear
		public static int JOY1_BUTTON_3_COLLECT_THE_GEAR = 3;
		// button 5 moves the arm down
		public static int JOY1_BUTTON_5_MOVE_ARM_DOWN = 5;
		// button 6 moves the arm up
		public static int JOY1_BUTTON_6_MOVE_ARM_UP = 6;
// Fuel
	// SPARK PWM IDs
		public static int FUEL_FURNACE_ROLLER_MOTOR = 0;
		public static int FUEL_FURNACE_OUT_DIR = 1;
		public static int FUEL_LOAD_STATION_ROLLER_MOTOR = 2;
		public static int FUEL_LOAD_STATION_IN_DIR = 1;
		public static int FUEL_CONVEYOR_ROLLER_MOTOR = 3;
	// Button IDs
		// Button 6 collects the fuel off of the floor
		public static int JOY1_BUTTON_4_FLOOR_FUEL_COLLECTOR = 4;
		// Button 7 collects the fuel from the load station
		public static int JOY1_BUTTON_7_LOAD_STATION_COLLECTOR = 7;
		// Button 8 dispenses the fuel into the furnace
		public static int JOY1_BUTTON_8_FURNACE_ROLLER_DISPENSER = 8;
	// Other stuffs

// Drivebase
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
	// CANTalon ID
		public static int ROBOT_CLIMBER = 8;
	// Button ID
	// button 9 on joystick 2 turns the climb motor on and off
		public static int JOY1_BUTTON_11_CLIMB_ON_AND_OFF = 11;


//		public static
}