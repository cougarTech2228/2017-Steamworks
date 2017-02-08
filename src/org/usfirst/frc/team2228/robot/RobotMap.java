package org.usfirst.frc.team2228.robot;

public final class RobotMap
{
// Joysticks
		public static int RIGHT_SIDE_JOYSTICK_ONE = 0;
		public static int LEFT_SIDE_JOYSTICK_ONE = 1;

// Gear
	// CANTalon ID's
		public static int LEFT_GEAR_COLLECTOR = 6;
		public static int RIGHT_GEAR_COLLECTOR = 7;
		public static int GEAR_ARM = 5;
	// Button ID's
		// button 2 lowers the arm and releases the gear at the same time
		public static int JOY1_BUTTON_2_DROP_THE_GEAR = 2;
		// button 3 releases the gear
		public static int JOY1_BUTTON_3_RELEASE_THE_GEAR = 3;
		// button 4 collects the gear
		public static int JOY1_BUTTON_4_COLLECT_THE_GEAR = 4;
		// button 5 moves the arm down
		public static int JOY1_BUTTON_5_MOVE_ARM_DOWN = 5;
		// button 6 moves the arm up
		public static int JOY1_BUTTON_6_MOVE_ARM_UP = 6;

// Drivebase
	// CANTalon ID's
		public static int RIGHT_ONE_DRIVE = 1;
		public static int RIGHT_TWO_DRIVE = 2;
		public static int LEFT_ONE_DRIVE = 3;
		public static int LEFT_TWO_DRIVE = 4;
	// Button ID's
		// button 8 on joystick 2 increases the speed of the robot
		public static int JOY2_BUTTON_8_INCREASE_SPEED = 8;
		// button 7 on joystick 2 decreases the speed of the robot
		public static int JOY2_BUTTON_7_DECREASE_SPEED = 7;

// Climb
	// CANTalon ID
		public static int ROBOT_CLIMBER = 8;
	// Button ID
	// button 9 on joystick 2 turns the climb motor on and off
		public static int JOY1_BUTTON_9_CLIMB_ON_AND_OFF = 9;

// *Fuel
	// SPARK PWM IDs
		public static int FUEL_FURNACE_ROLLER_MOTOR = 0;
		public static int FUEL_LOAD_STATION_ROLLER_MOTOR = 2;
		public static int FUEL_CONVEYOR_ROLLER_MOTOR = 3;
	// Button IDs
		// Button 1 collects the fuel off of the floor
		public static int JOY1_BUTTON_1_FLOOR_FUEL_COLLECTOR = 1;
		public static int JOY1_BUTTON_7_LOAD_STATION_COLLECTOR = 7;
	// Other stuffs
//		public static
}
// * means not changed because have not done yet