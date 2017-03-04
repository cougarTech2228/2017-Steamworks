package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gear {
	private CANTalon gearArm;
	private CANTalon gearJaw;
	private Spark gearLoadCollectionGuide;
	private boolean gearCollection = false;
	private boolean moveGearArmUp = false;
	private boolean moveGearArmDown = false;
	private boolean gearGraspRelease = false;
	private Joystick joystick;
	private double gearArmDownXbox;
	private double gearArmUpXbox;
	private boolean dropDaGear = false;
	private double competitionArmUp = 1;
	private double competitionArmDown = 1;
	private double competitionGearCollectionValue = 1;
	private double competitionGearReleaseValue = 1;
	private double competitionDropArm = 1;
	private double competitionDropRelease = 1;
	private double armUp = -0.4;
	private double armDown = 0.2;
	private double gearJawOpenValue = -0.3;
	private double gearJawCloseValue = 0.3;

	// Constructor
	public Gear(Joystick joy) {

		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		// leftGearCollector = new CANTalon(RobotMap.LEFT_GEAR_COLLECTOR);
		gearJaw = new CANTalon(RobotMap.GEAR_JAW);
		gearLoadCollectionGuide = new Spark(RobotMap.GEAR_LOAD_STATION_GUIDE);

	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit() {

	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic() {

	}

	// Called continuously during the teleop period
	public void teleopPeriodic() {
		gearCollection = joystick.getRawButton(RobotMap.BUTTON_2_COLLECT_THE_GEAR);
		gearGraspRelease = joystick.getRawButton(RobotMap.BUTTON_3_RELEASE_THE_GEAR);
		if (gearCollection) {
			// leftGearCollector.set(-0.9);
			gearJaw.set(gearJawOpenValue);
		} else if (gearGraspRelease) {
			// leftGearCollector.set(.9);

			gearJaw.set(gearJawCloseValue);
		} else {
			gearJaw.set(0);
		}
		// else
		// {
		// // leftGearCollector.set(0);
		// gearCollector.set(0);
		// }

		// gearArmUpXbox = joystick.getRawAxis(3);
		// gearArmDownXbox = joystick.getRawAxis(2);
		moveGearArmUp = joystick.getRawButton(RobotMap.BUTTON_4_MOVE_ARM_UP);
		moveGearArmDown = joystick.getRawButton(RobotMap.BUTTON_1_MOVE_ARM_DOWN);
		if (moveGearArmUp)

		{
			gearArm.set(armUp);
		} else if (moveGearArmDown) {
			gearArm.set(armDown);
		} else {
			gearArm.set(0);
			// leftGearCollector.set(0);
			// gearCollector.set(0);
		} //

		SmartDashboard.putNumber("gearJaw Current", gearJaw.getOutputCurrent());

	}

	public void gearClawSet(double vel) {
		gearJaw.set(vel);
	}

	public void gearArmSet(double vel) {
		gearArm.set(vel);
	}

	// Called continuously during testing
	public void testPeriodic() {

	}
}
//