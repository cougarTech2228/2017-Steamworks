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
	private Spark guide;
	private boolean floorGearCollection = false;
	private boolean moveGearArmUp = false;
	private boolean moveGearArmDown = false;
	private boolean gearGraspRelease = false;
	private boolean gearGuideCollect = false;
	private double open;
	private double guideUp = -0.2;
	private double guideDown = 0.4;
	private Joystick joystick;
	private boolean dropDaGear = false;
	private double armUp = -0.7;
	private double armDown = 0.2;
	private double gearCollectionValue = 0.4;
	private double gearReleaseValue = -0.4;
	private double dropArm = -0.2;
	private double dropRelease = 0.5;
	private double rollerVelocity = -0.5;
	private AnalogInput potentiometerArm;
	private AnalogInput potentiometerJaw;
	private DigitalInput fwdLimitSwitch;
	private DigitalInput revLimitSwitch;

	// Constructor
	public Gear(Joystick joy) {
		guide = new Spark(/*RobotMap.GEAR_GUIDE*/ 9);
		joystick = joy;
		gearArm = new CANTalon(/*RobotMap.GEAR_ARM*/6);
		// leftGearCollector = new CANTalon(RobotMap.LEFT_GEAR_COLLECTOR);
		gearJaw = new CANTalon(/*RobotMap.GEAR_JAW*/5);
		
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
	public void teleopPeriodic(Fuel fuel) {

		floorGearCollection = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_3_FLOOR_GEAR_COLLECT*/2);
		gearGraspRelease = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_2_RELEASE_THE_GEAR*/3);
		
		SmartDashboard.putNumber("ArmPotentiometer", potentiometerArm.getValue());
		SmartDashboard.putNumber("JawPotentiometer", potentiometerJaw.getValue());
		
		if (floorGearCollection) {
			// leftGearCollector.set(-0.9);
			// gearJaw.set(gearCollectionValue);
			gearJaw.set(-gearCollectionValue);
		} else if (gearGraspRelease) {
			// leftGearCollector.set(.9);

			// gearJaw.set(gearReleaseValue);
			gearJaw.set(-gearReleaseValue);
		} else {
			// gearJaw.set(0);
			gearJaw.set(0);
		}
		// else
		// {
		// // leftGearCollector.set(0);
		// gearCollector.set(0);
		// }

		moveGearArmUp = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_6_MOVE_ARM_UP*/4);
		moveGearArmDown = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_5_MOVE_ARM_DOWN*/1);
		if (moveGearArmUp)

		{
			// gearArm.set(armUp);
			gearArm.set(armUp);
		} else if (moveGearArmDown) {
			// gearArm.set(armDown);
			gearArm.set(armDown*2);
		} else {
			// gearArm.set(0);
			gearArm.set(0);
			// leftGearCollector.set(0);
			// gearCollector.set(0);
		} //
		gearGuideCollect = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_1_GEAR_GUIDE_COLLECT*/6);
		
		if(gearGuideCollect){
			if(fwdLimitSwitch.get()){
//				guide.set(guideDown);
			}
			fuel.fuelLoadStationRollerSet(rollerVelocity);
			
//			gearArmSet(armUp);
//			if (gearArm.isRevLimitSwitchClosed()){
//				gearJaw.set(open);
//			}
		}else{
			
			if(revLimitSwitch.get()){
//				guide.set(guideUp);
			}
//			fuel.fuelLoadStationRollerSet(0);
			
		}
		
		gearGuideCollect = false;

		// dropDaGear = joystick
		// .getRawButton(RobotMap.JOY1_BUTTON_1_DROP_THE_GEAR);
		// if (dropDaGear)
		// {
		// gearArm.set(dropArm);
		// gearCollector.set(dropRelease);
		// }
		// SmartDashboard.putNumber("gearJaw Current",
		// gearJaw.getOutputCurrent());
		

	}
	
	public boolean loadingGear(){
		
		return joystick.getRawButton(6);
	}
	
	public void gearClawSet(double vel) {
		// gearJaw.set(vel);
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