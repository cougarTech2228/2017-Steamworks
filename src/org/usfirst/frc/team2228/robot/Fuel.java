package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Fuel
{
	private Spark fuelFurnaceRoller;
	private Spark fuelLoadStationRoller;
	private Spark fuelConveyorRoller;
	private Joystick joystick;
	private Joystick joystick2;
	private boolean firstConveyorValue = false;
	private boolean lastConveyorValue = false;
	private boolean firstLoadStationValue = false;
	private boolean lastLoadStationValue = false;
	private boolean firstFurnaceValue = false;
	private boolean lastFurnaceValue = false;
	private boolean conveyorMotorValue = false;
	private boolean loadStationMotorValue = false;
	private boolean furnaceMotorValue = false;
	private final double conveyorSpeed = 1;
	private final double loadStationSpeed = 1;
	private final double furnaceDispenserSpeed = 1;
	private PowerDistributionPanel pdp;
	private double theConveyorNowCurrent;
	private double theLoadStationNowCurrent;
	private double theFurnaceNowCurrent;

	// Constructor
	public Fuel(Joystick joy, PowerDistributionPanel pdpCurrent)
	{
		pdp = pdpCurrent;
		joystick = joy;
		// joystick2 = joy;
		fuelFurnaceRoller = new Spark(RobotMap.FUEL_FURNACE_ROLLER_MOTOR);
		fuelLoadStationRoller = new Spark(
				RobotMap.FUEL_LOAD_STATION_ROLLER_MOTOR);
		fuelConveyorRoller = new Spark(RobotMap.FUEL_CONVEYOR_ROLLER_MOTOR);
		SmartDashboard.putBoolean("Fuel Roller Power", false);
		SmartDashboard.putBoolean("Fuel Load Station Roller Power", false);
		SmartDashboard.putBoolean("Furnace Roller Power", false);
	}

	// Called once at the beginning of the autonomous period
	public void autonomousInit()
	{

	}

	// Called continuously during the autonomous period
	public void autonomousPeriodic()
	{

	}

	// Called continuously during the teleop period
	public void teleopPeriodic()
	{
		firstConveyorValue = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_4_FLOOR_FUEL_COLLECTOR);

		if (firstConveyorValue != lastConveyorValue)
		{
			if (firstConveyorValue == true)
			{
				if (conveyorMotorValue == false)
				{
					fuelConveyorRoller.set(conveyorSpeed);
					SmartDashboard.putBoolean("Fuel Roller Power", true);
					conveyorMotorValue = true;
				}
				else
				{
					fuelConveyorRoller.set(0);
					SmartDashboard.putBoolean("Fuel Roller Power", false);
					conveyorMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theConveyorNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Conveyor Current",
				theConveyorNowCurrent);

		if (pdp.getCurrent(0) >= ConstantMap.MAX_CONVEYOR_CURRENT)
		{
			fuelConveyorRoller.set(0);
			SmartDashboard.putBoolean("Fuel Conveyor Emergency Shut Down",
					true);
		}
		// >o<

		firstLoadStationValue = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_7_LOAD_STATION_COLLECTOR);

		if (firstLoadStationValue != lastLoadStationValue)
		{
			if (firstLoadStationValue == true)
			{
				if (loadStationMotorValue == false)
				{
					fuelLoadStationRoller.set(loadStationSpeed);
					SmartDashboard.putBoolean("Fuel Load Station Roller Power",
							true);
					loadStationMotorValue = true;
				}
				else
				{
					fuelLoadStationRoller.set(0);
					SmartDashboard.putBoolean("Fuel Load Station Roller Power",
							false);
					loadStationMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theLoadStationNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Load Station Current",
				theLoadStationNowCurrent);

		if (pdp.getCurrent(0) >= ConstantMap.MAX_LOAD_STATION_CURRENT)
		{
			fuelLoadStationRoller.set(0);
			SmartDashboard.putBoolean("Fuel Load Station Emergency Shut Down",
					true);
		}
		// >_<

		firstFurnaceValue = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_8_FURNACE_ROLLER_DISPENSER);

		if (firstFurnaceValue != lastFurnaceValue)
		{
			if (firstFurnaceValue == true)
			{
				if (furnaceMotorValue == false)
				{
					fuelFurnaceRoller.set(furnaceDispenserSpeed
							* RobotMap.FUEL_FURNACE_OUT_DIR);
					SmartDashboard.putBoolean("Furnace Roller Power", true);
					furnaceMotorValue = true;
				}
				else
				{
					fuelFurnaceRoller.set(0);
					SmartDashboard.putBoolean("Furnace Roller Power", false);
					furnaceMotorValue = false;
				}
			}

			lastConveyorValue = firstConveyorValue;
		}

		theFurnaceNowCurrent = pdp.getCurrent(0);
		SmartDashboard.putNumber("Fuel Furnace Current", theFurnaceNowCurrent);

		if (pdp.getCurrent(0) >= ConstantMap.MAX_FURNACE_CURRENT)
		{
			fuelLoadStationRoller.set(0);
			SmartDashboard.putBoolean("Fuel Furnace Emergency Shut Down", true);
		}

	}

	// Called continuously during testing
	public void testPeriodic()
	{
		firstConveyorValue = joystick
				.getRawButton(RobotMap.JOY1_BUTTON_4_FLOOR_FUEL_COLLECTOR);

		if (firstConveyorValue != lastConveyorValue)
		{
			if (firstConveyorValue == true)
			{
				fuelConveyorRoller.set(conveyorSpeed);
				SmartDashboard.putBoolean("Fuel Roller Power", true);
			}
			else if (fuelConveyorRoller.get() == 1)
			{
				fuelConveyorRoller.set(0);
				SmartDashboard.putBoolean("Fuel Roller Power", false);
			}
			lastConveyorValue = firstConveyorValue;
		}
	}
}