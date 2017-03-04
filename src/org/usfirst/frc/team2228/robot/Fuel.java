package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Fuel
{

	private VictorSP fuelFurnaceRoller;
	private VictorSP fuelLoadStationRoller;
	private VictorSP fuelConveyorRoller;
	private Spark fuelGuide;
	private DigitalInput fwdLimitSwitch;
	private DigitalInput revLimitSwitch;
	private double guideUpSpeed = 0.3;
	private double guideDownSpeed = -0.3;
	private double fuelDischargeSpeed = -1;
	
	private Joystick joystick;
	private boolean firstConveyorValue = false;
	private boolean lastConveyorValue = false;
	private boolean firstLoadStationValue = false;
	private boolean lastLoadStationValue = false;
	private boolean firstFurnaceValue = false;
	private boolean lastFurnaceValue = false;
	private boolean conveyorMotorValue = false;
	private boolean loadStationMotorValue = false;
	private boolean furnaceMotorValue = false;

	private final double conveyorSpeed = -1;
	private final double loadStationSpeed = 1;
	private final double furnaceDispenserSpeed = 1;
	private PowerDistributionPanel pdp;

	private double theConveyorNowCurrent;
	private double theLoadStationNowCurrent;
	private double theFurnaceNowCurrent;
	private boolean newValue;
	private boolean oldValue;
	private boolean conveyorOn;

	// Constructor
	public Fuel(Joystick joy, PowerDistributionPanel pdpCurrent)
	{
		newValue = false;
		oldValue = false;
		conveyorOn = false;
		pdp = pdpCurrent;
		joystick = joy;
		
		fuelGuide = new Spark(8);
		
		fwdLimitSwitch = new DigitalInput(8);
		revLimitSwitch = new DigitalInput(9);
		
		fuelFurnaceRoller = new VictorSP(RobotMap.FUEL_FURNACE_ROLLER_MOTOR);
		fuelLoadStationRoller = new VictorSP(
				RobotMap.FUEL_LOAD_STATION_ROLLER_MOTOR);
		fuelConveyorRoller = new VictorSP(RobotMap.FUEL_CONVEYOR_ROLLER_MOTOR);
		
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
				.getRawButton(RobotMap.BUTTON_5_LOAD_STATION_COLLECTOR);

		if (firstLoadStationValue != lastLoadStationValue)
		{
			if (firstLoadStationValue == true)
			{
				if (loadStationMotorValue == false)
				{
					fuelLoadStationRoller.set(testBotLoadStationSpeed);
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
				.getRawButton(RobotMap.BUTTON_7_FURNACE_ROLLER_DISPENSER);

		if (firstFurnaceValue != lastFurnaceValue)
		{
			if (firstFurnaceValue == true)
			{
				if (furnaceMotorValue == false)
				{
					fuelFurnaceRoller.set(furnaceDispenserSpeed
							* ConstantMap.FUEL_FURNACE_OUT_DIR);
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

	public void fuelFurnaceRollerSet(double vel){
		
		fuelFurnaceRoller.set(vel);
		
	}
	
	public void fuelLoadStationRollerSet(double vel){
		
		fuelLoadStationRoller.set(vel);
		
	}
	
	public void collectFuel(){
		
		fuelLoadStationRoller.set(1);
		if(fwdLimitSwitch.get()){
//			fuelGuide.set(guideDownSpeed);
		}
		
	}
	
	public void guidesUp(){
		
		if(revLimitSwitch.get()){
//			fuelGuide.set(guideUpSpeed);
		}
		
	}
	
	public void dischargeFuel(){
		
		fuelFurnaceRoller.set(fuelDischargeSpeed);
		
	}
	
	public void collectFuelFloor(){
		
		
		newValue = joystick.getRawButton(/*RobotMap.JOY1_BUTTON_11_CLIMB_ON_AND_OFF*/9);

		if (newValue != oldValue) {
			
			if (newValue) {
				
				if (!conveyorOn) {
					
					fuelConveyorRoller.set(conveyorSpeed);
					conveyorOn = true;
					
				}else {
					
					fuelConveyorRoller.set(0);
					conveyorOn = false;
					
				}

			}
			oldValue = newValue;
		}
		
	}
	
	
	// Called continuously during testing
	public void testPeriodic()
	{
		
	}
}
