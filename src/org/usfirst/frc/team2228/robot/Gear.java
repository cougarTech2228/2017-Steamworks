package org.usfirst.frc.team2228.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
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
	private boolean gearGuideCollect = false;
	private double open;
	private double guideUp = -0.2;
	private double guideDown = 0.4;
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
	private double gearJawOpenValue = -0.3;
	private double gearJawCloseValue = 0.3;
	private double armUp = -0.7;
	private double armDown = 0.4;

	private double gearCollectionValue = -0.4;
	private double gearReleaseValue = 0.4;
	private double rollerVelocity = -0.5;
	private AnalogInput potentiometerArm;
	private AnalogInput potentiometerJaw;
	private DigitalInput fwdLimitSwitch;
	private DigitalInput revLimitSwitch;

	// Constructor
	public Gear(Joystick joy) {
		gearLoadCollectionGuide = new Spark(RobotMap.GEAR_LOAD_STATION_GUIDE);
		joystick = joy;
		gearArm = new CANTalon(RobotMap.GEAR_ARM);
		gearJaw = new CANTalon(RobotMap.GEAR_JAW);

		potentiometerArm = new AnalogInput(1);
		potentiometerJaw = new AnalogInput(2);
		SmartDashboard.putNumber("ArmPotentiometer", potentiometerArm.getValue());
		SmartDashboard.putNumber("JawPotentiometer", potentiometerJaw.getValue());

		fwdLimitSwitch = new DigitalInput(6);
		revLimitSwitch = new DigitalInput(7);

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
			gearJaw.set(gearCollectionValue);
		} else if (gearGraspRelease) {
			gearJaw.set(gearReleaseValue);
		} else {
			gearJaw.set(0);
		}

		moveGearArmUp = joystick.getRawButton(RobotMap.BUTTON_4_MOVE_ARM_UP);
		moveGearArmDown = joystick.getRawButton(RobotMap.BUTTON_1_MOVE_ARM_DOWN);

		if (moveGearArmUp) {
			// gearArm.set(armUp);
			gearArm.set(armUp);
		} else if (moveGearArmDown) {
			gearArm.set(armDown);
		} else {
			gearArm.set(0);
			// leftGearCollector.set(0);
			// gearCollector.set(0);
		} //

		gearGuideCollect = joystick.getRawButton(RobotMap.BUTTON_6_GEAR_COLLECTION);

		if (gearGuideCollect) {
			if (fwdLimitSwitch.get()) {
				// guide.set(guideDown);
			}
			// put the button 6 requiremment in fuel
			// fuel.fuelLoadStationRollerSet(rollerVelocity);

			// gearArmSet(armUp);
			// if (gearArm.isRevLimitSwitchClosed()){
			// gearJaw.set(open);
			// }
		} else {

			if (revLimitSwitch.get()) {
				// guide.set(guideUp);
			}
			// fuel.fuelLoadStationRollerSet(0);

		}

		gearGuideCollect = false;

	}

	public boolean loadingGear() {

		return joystick.getRawButton(6);
	}

	public void gearClawSet(double vel) {
		gearJaw.set(vel);
	}

	public void gearArmSet(double vel) {
		gearArm.set(vel);
		// gearJaws.set(vel);
	}

	public void raiseTheArm() {
		if (gearArm.isFwdLimitSwitchClosed()) {
			gearArmSet(armUp);

		} else if (!gearArm.isRevLimitSwitchClosed() && !gearArm.isFwdLimitSwitchClosed()) {
			gearArm.set(armUp);
		} else {

		}
	}

	// Called continuously during testing
	public void testPeriodic() {

	}
}
//
