package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Fuel {

	private XboxIF xbox;

	private PowerDistributionPanel pdp;
	private VictorSP fuelFurnaceRoller;
	private VictorSP fuelLoadStationRoller;
	private VictorSP fuelConveyorRoller;
	private boolean firstConveyorValue = false;
	private boolean lastConveyorValue = false;
	private boolean firstLoadStationValue = false;
	private boolean lastLoadStationValue = false;
	private boolean firstFurnaceValue = false;
	private boolean lastFurnaceValue = false;
	private boolean conveyorMotorValue = false;
	private boolean loadStationMotorValue = false;
	private boolean furnaceMotorValue = false;
	private final double competitionConveyorSpeed = 1;
	private final double competitionLoadStationSpeed = 1;
	private final double competitionFurnaceDispenserSpeed = 1;

	private final double testBotConveyorSpeed = 1;
	private final double testBotLoadStationSpeed = 1;
	private final double testBotFurnaceDispenserSpeed = 1;
	private double theConveyorNowCurrent;
	private double theLoadStationNowCurrent;
	private double theFurnaceNowCurrent;

	// Constructor

	public Fuel(XboxIF xbox, PowerDistributionPanel pdpCurrent) {

		pdp = pdpCurrent;
		xbox = xbox;
		// joystick2 = joy;
		fuelFurnaceRoller = new VictorSP(RobotMap.FUEL_FURNACE_ROLLER_MOTOR);
		fuelLoadStationRoller = new VictorSP(RobotMap.FUEL_LOAD_STATION_ROLLER_MOTOR);
		fuelConveyorRoller = new VictorSP(RobotMap.FUEL_CONVEYOR_ROLLER_MOTOR);
		SmartDashboard.putBoolean("Fuel Roller Power", false);
		SmartDashboard.putBoolean("Fuel Load Station Roller Power", false);
		SmartDashboard.putBoolean("Furnace Roller Power", false);
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit() {

	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic() {

	}

	// Called continuously during the teleop period
	public void teleopPeriodic() {

		if (firstConveyorValue != lastConveyorValue) {
			if (firstConveyorValue == true) {
				if (conveyorMotorValue == false) {
					fuelConveyorRoller.set(testBotConveyorSpeed);
					SmartDashboard.putBoolean("Fuel Roller Power", true);
					conveyorMotorValue = true;
				} else {
					fuelConveyorRoller.set(0);
					SmartDashboard.putBoolean("Fuel Roller Power", false);
					conveyorMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theConveyorNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Conveyor Current", theConveyorNowCurrent);

		if (pdp.getCurrent(0) >= ConstantMap.MAX_CONVEYOR_CURRENT) {
			fuelConveyorRoller.set(0);
			SmartDashboard.putBoolean("Fuel Conveyor Emergency Shut Down", true);
		}
		// >o<

		boolean toggle = true;
		firstLoadStationValue = xbox.getCollectFuelButton(toggle);
		

		if (firstLoadStationValue != lastLoadStationValue) {
			if (firstLoadStationValue == true) {
				if (loadStationMotorValue == false) {
					fuelLoadStationRoller.set(testBotLoadStationSpeed);
					SmartDashboard.putBoolean("Fuel Load Station Roller Power", true);
					loadStationMotorValue = true;
				} else {
					fuelLoadStationRoller.set(0);
					SmartDashboard.putBoolean("Fuel Load Station Roller Power", false);
					loadStationMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theLoadStationNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Load Station Current", theLoadStationNowCurrent);


		if (pdp.getCurrent(0) >= ConstantMap.MAX_LOAD_STATION_CURRENT) {
			fuelLoadStationRoller.set(0);
			SmartDashboard.putBoolean("Fuel Load Station Emergency Shut Down", true);
		}

		// >_<


		firstFurnaceValue = xbox.getReleaseFuelButton(toggle);

		if (firstFurnaceValue != lastFurnaceValue) {
			if (firstFurnaceValue == true) {
				if (furnaceMotorValue == false) {
					fuelFurnaceRoller.set(testBotFurnaceDispenserSpeed * ConstantMap.FUEL_FURNACE_OUT_DIR);
					SmartDashboard.putBoolean("Furnace Roller Power", true);
					furnaceMotorValue = true;
				} else {
					fuelFurnaceRoller.set(0);
					SmartDashboard.putBoolean("Furnace Roller Power", false);
					furnaceMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theFurnaceNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Furnace Current", theFurnaceNowCurrent);

		if (pdp.getCurrent(0) >= ConstantMap.MAX_FURNACE_CURRENT) {
			fuelLoadStationRoller.set(0);
			SmartDashboard.putBoolean("Fuel Furnace Emergency Shut Down", true);
		}

	}

	// Called continuously during testing
	public void testPeriodic() {

		if (firstConveyorValue != lastConveyorValue) {
			if (firstConveyorValue == true) {
				fuelConveyorRoller.set(testBotConveyorSpeed);
				SmartDashboard.putBoolean("Fuel Roller Power", true);
			} else if (fuelConveyorRoller.get() == 1) {
				fuelConveyorRoller.set(0);
				SmartDashboard.putBoolean("Fuel Roller Power", false);
			}
			lastConveyorValue = firstConveyorValue;
		}
	}
}