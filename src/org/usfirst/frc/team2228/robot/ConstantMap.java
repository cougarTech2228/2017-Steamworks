package org.usfirst.frc.team2228.robot;

public final class ConstantMap {

	// *Constants
	public static final String doNothing = "Do Nothing";
	public static final String baseLineTime = "baseLineTime";

	public static final double MAX_CONVEYOR_CURRENT = 5;
	public static final double MAX_LOAD_STATION_CURRENT = 6;
	public static final double MAX_FURNACE_CURRENT = 7;

	public static int FUEL_LOAD_STATION_IN_DIR = 1;
	public static int FUEL_FURNACE_OUT_DIR = -1;


	// CTRE CIMCoder magnetic quadrature (4) encoder 20 pulse per turn
	//  AndyMark tough box mini 14:50 to 16:48 
	// 857 = (50/14)*(48/16)*20*4
	public static final double COUNTS_PER_REV = 857.0;
	// gear box changes to 14:50 19:45 
	public static final double FASTER_COUNTS_PER_REV = 676.0;
	public static final double WHEEL_DIAMETER = 6.0; // inches
	
	public static final double COUNTS_INCH = COUNTS_PER_REV / (WHEEL_DIAMETER * Math.PI);
	public static final double FAST_COUNTS_INCH = FASTER_COUNTS_PER_REV / (WHEEL_DIAMETER * Math.PI);
	

	public enum AutoChoices {
		DO_NOTHING, BASE_LINE_TIME_SENSOR, CENTER_GEAR_PLACEMENT, 
		RIGHT_GEAR_PLACEMENT, LEFT_GEAR_PLACEMENT, VISION_GEAR_LEFT, 
		VISION_GEAR_RIGHT, CENTER, GEAR_PLACEMENT_DREAM, GEAR_AND_FUEL_PLACEMENT_LEFT, DRIVE_TO_DA_PIN, GEAR_AND_FUEL_PLACEMENT_RIGHT
	}

}
