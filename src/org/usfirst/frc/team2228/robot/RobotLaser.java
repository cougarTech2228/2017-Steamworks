package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotLaser extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	int testLoopCounter;
	SPI arduinoSPI;
	String autoSelected;
	CANTalon right1;
	CANTalon left1;
	CANTalon right2;
	CANTalon left2;
	Joystick joy1;
	Joystick joy2;
	RobotDrive joydrive;
	private Gear gear;
	private Balls balls;
	private Climb climb;
	private Drive drive;
	SendableChooser<String> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		testLoopCounter = 0;
		arduinoSPI = new SPI(Port.kOnboardCS0);
		arduinoSPI.setClockRate(500000);
		arduinoSPI.setClockActiveHigh();
		arduinoSPI.setChipSelectActiveLow();
		arduinoSPI.setMSBFirst();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		gear = new Gear();
		balls = new Balls();
		climb = new Climb();
		drive = new Drive();
	    right1 = new CANTalon(1); //2
	    right2 = new CANTalon(2); //1
	    left1 = new CANTalon(3); //3
	    left2 = new CANTalon(4); //4
	    joydrive = new RobotDrive(right1, left1);
	    right2.changeControlMode(TalonControlMode.Follower);
	    right2.enableControl();
	    right2.set(right1.getDeviceID());
	    left2.changeControlMode(TalonControlMode.Follower);
	    left2.enableControl();
	    left2.set(left1.getDeviceID());
	    joy1 = new Joystick(0);
	    joy2 = new Joystick(1);
	    SmartDashboard.putNumber ("current right leader", 0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		joydrive.tankDrive(joy1, joy2);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//System.out.println("whatever");
		testLoopCounter++;
		 
        byte[] inputBuffer;
        byte[] outputBuffer;
        int lowByte;
        int highByte;
        int LIDARdistance;
        
        inputBuffer = new byte[1];
        outputBuffer = new byte[1];

        if( (testLoopCounter % 50) == 0 )  
        {
          // set output buffer to send the first command (ASCII 'r' - read the laser rangefinder low byte)
          outputBuffer[0] = 'r';
          // send the command byte
          arduinoSPI.write(outputBuffer, 1);
          
          // set output buffer to send the second command (ASCII 's' - read the laser rangefinder high byte)
          outputBuffer[0] = 's';
          // send the value 
          arduinoSPI.transaction(outputBuffer, inputBuffer, 1);
          
          lowByte = (int)inputBuffer[0];
          if( lowByte < 0 )
          {
        	  lowByte = lowByte + 256;
          }

          // now just read the last value (no write)
          arduinoSPI.read(true, inputBuffer, 1);

          highByte = (int)inputBuffer[0];
          highByte = (int)(highByte * 256);

          LIDARdistance = (int)(lowByte + highByte);
          
          System.out.println(LIDARdistance);
          
        }

    }
	
}

