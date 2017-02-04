package org.usfirst.frc.team2228.robot;

public final class RobotMap
{
// Joysticks
		public static int RIGHT_SIDE_JOYSTICK_ONE = 0;
		public static int LEFT_SIDE_JOYSTICK_ONE = 1;

// Drivebase
	// CANTalon IDs
		public static int RIGHT_ONE_DRIVE = 1;
		public static int RIGHT_TWO_DRIVE = 2;
		public static int LEFT_ONE_DRIVE = 3;
		public static int LEFT_TWO_DRIVE = 4;
	// Button IDs
		// Button 1 switches the type of controls between chessyDrive (arcadeDrive) to tankDrive
		public static int JOY2_BUTTON_1_DRIVE_TYPE_SWITCH = 1;
		// Button 7 decreases the speed of the robot
		public static int JOY2_BUTTON_7_SPEED_DECREASE = 7;
		// Button 8 increases the speed of the robot
		public static int JOY2_BUTTON_8_SPEED_INCREASE = 8;

// Climb
	// CANTalon ID
		public static int ROBOT_CLIMBER = 8;
	// Button ID
		// Button 9 turns the climb motor on and off
		public static int JOY1_CLIMB_MOTOR_POWER = 9;

// Gear
	// CANTalon IDs
		public static int LEFT_GEAR_COLLECTOR = 6;
		public static int RIGHT_GEAR_COLLECTOR = 7;
		public static int GEAR_ARM = 5;
	// Button IDs
		// Button 2 moves the gear arm and releases the gear at the same time
		public static int JOY1_BUTTON_2_DROP_THE_GEAR = 2;
		// Button 3 releases the gear
		public static int JOY1_BUTTON_3_RELEASE_THE_GEAR = 3;
		// Button 4 collects the gear
		public static int JOY1_BUTTON_4_COLLECT_THE_GEAR = 4;
		// Button 5 moves the gear down
		public static int JOY1_BUTTON_5_MOVE_THE_GEAR_ARM_UP = 5;
		// Button 6 moves the gear up
		public static int JOY1_BUTTON_6_MOVE_THE_GEAR_ARM_DOWN = 6;
	
	// *Fuel

}
// * means not changed because have not done yet