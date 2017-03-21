package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class XboxIF {
	private XboxController xbox;
	public  Hand leftHand;
	public  Hand rightHand;

	private boolean armUp = false;
	private boolean armDown = false;
	private boolean collectGear = false;
	private boolean releaseGear = false;
	private boolean collectFuel = false;
	private boolean releaseFuel = false;

	private boolean climbOff;
	private boolean climbCurrentState;
	private boolean climbLastState;

	private boolean armLastState;
	private boolean armCurrentState;
	private boolean armOff;

	private boolean armDownLastState;
	private boolean armDownCurrentState;
	private boolean armDownOff;

	private boolean gearLastState;
	private boolean gearCurrentState;
	private boolean gearOff;

	private boolean gearCloseLastState;
	private boolean gearCloseCurrentState;
	private boolean gearCloseOff;

	private boolean collectFuelLastState;
	private boolean collectFuelCurrentState;
	private boolean collectFuelOff;

	private boolean releaseFuelLastState;
	private boolean releaseFuelCurrentState;
	private boolean releaseFuelOff;

	private boolean collectGearLoadStationLastState;
	private boolean collectGearLoadStationCurrentState;
	private boolean collectGearLoadStationOff;
	
	int _channel;

	public enum State {
		TOGGLE, NOT_TOGGLED
	}

	public XboxIF(final int channel) {
		xbox = new XboxController(channel);
		_channel = channel; 
		// public static boolean BUTTON_ONE = xbox.getAButton();
		// boolean BUTTON_TWO = xbox.getBButton();
		// boolean BUTTON_THREE = xbox.getXButton();
		// boolean BUTTON_FOUR = xbox.getYButton();
		// boolean BUTTON_FIVE = xbox.getBumper(leftHand);
		// boolean BUTTON_SIX = xbox.getBumper(rightHand);
		// boolean BUTTON_SEVEN = xbox.getBackButton();
		// boolean BUTTON_EIGHT = xbox.getStartButton();
		// boolean BUTTON_NINE = xbox.getStickButton(leftHand);
		// boolean BUTTON_TEN = xbox.getStickButton(rightHand);
	}

	public double leftStickX() {
		return xbox.getX(leftHand);
	}

	public double rightStickX() {
		return xbox.getX(rightHand);
	}

	public double leftStickY() {
		return xbox.getY(leftHand);

	}

	public double rightStickY() {
		return xbox.getY(rightHand);
	}

	public boolean getArmUpButton(boolean toggle) {
		armCurrentState = xbox.getYButton();
		boolean returnState = false;
		if (armCurrentState = true) {
			if (!toggle || armLastState == false) {

				returnState = true;
			}

		}
		armLastState = armCurrentState;
		return returnState;

	}

	public boolean getMoveArmDownButton(boolean toggle) {
		armDownCurrentState = xbox.getAButton();
		boolean returnState = false;
		if (armDownCurrentState = true) {
			if (!toggle || armDownLastState == false) {

				returnState = true;
			}

		}
		armDownLastState = armDownCurrentState;
		return returnState;
	}

	public boolean getOpenGearButton(boolean toggle) {

		gearCurrentState = xbox.getBumper(rightHand);
		boolean returnState = false;
		if (gearCurrentState = true) {
			if (!toggle || gearLastState == false) {

				returnState = true;
			}

		}
		gearLastState = gearCurrentState;
		return returnState;
	}

	public boolean getCloseGearButton(boolean toggle) {
		gearCloseCurrentState = xbox.getBButton();
		boolean returnState = false;
		if (gearCloseCurrentState = true) {
			if (!toggle || gearCloseLastState == false) {

				returnState = true;
			}

		}
		gearCloseLastState = gearCloseCurrentState;
		return returnState;
	}

	public boolean getCollectFuelButton(boolean toggle) {
		collectFuelCurrentState = xbox.getBumper(leftHand);
		boolean returnState = false;
		if (collectFuelCurrentState = true) {
			if (!toggle || collectFuelLastState == false) {

				returnState = true;
			}

		}
		collectFuelLastState = collectFuelCurrentState;
		return returnState;
	}

	public boolean getReleaseFuelButton(boolean toggle) {
		releaseFuelCurrentState = xbox.getBackButton();
		boolean returnState = false;
		if (releaseFuelCurrentState = true) {
			if (!toggle || releaseFuelLastState == releaseFuelOff) {

				returnState = true;
			}

		}
		releaseFuelLastState = releaseFuelCurrentState;
		return returnState;
	}

	public boolean getCollectGearLoadStationButton(boolean toggle) {
		collectGearLoadStationCurrentState = xbox.getBumper(rightHand);
		boolean returnState = false;
		if (collectGearLoadStationCurrentState = true) {
			if (!toggle || collectGearLoadStationLastState == false) {

				returnState = true;
			}

		}
		collectGearLoadStationLastState = collectGearLoadStationCurrentState;
		return returnState;
	}

	public boolean getClimbOnButton(boolean toggle) {
		climbCurrentState = xbox.getStartButton();
		boolean returnState = false;
		if (climbCurrentState = true) {
			if (!toggle || climbLastState == false) {

				returnState = true;
			}

		}
		climbLastState = climbCurrentState;
		return returnState;

	}

	public double getSpeedIncrease() {
		return xbox.getTriggerAxis(leftHand);
	}

	public double getSpeedDecrease() {
		return xbox.getTriggerAxis(rightHand);
	}
	public boolean getPOVUp(){
		return (xbox.getPOV(_channel) == 0);
	}
	public boolean getPOVDown(){
		return (xbox.getPOV(_channel) == 90);
	}
	public boolean getPOVLeft(){
		return (xbox.getPOV(_channel) == 180);
	}
	public boolean getPOVRight(){
		return (xbox.getPOV(_channel) == 270);
	}
	public int getPOVBoi(){
		return xbox.getPOV(_channel);
	}
}
