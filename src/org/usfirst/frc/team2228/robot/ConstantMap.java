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

	public static int timePeriodSF = 5;

	public enum AutoChoices {
		DO_NOTHING, BASE_LINE_TIME_SENSOR, BASE_LINE_ENCODERS, CENTER, RIGHT_GEAR_PLACEMENT, LEFT_GEAR_PLACEMENT, CENTER_GEAR_PLACEMENT, VISION_GEAR_RIGHT, VISION_GEAR_LEFT
	}

}