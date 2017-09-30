package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot
{
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	private AutoChoices choisir;
	// Carrying the classes from this project's library
	private Joystick joystick;
	private Gear gear;
	private Fuel fuel;
	private Climb climb;
	private Drive drive;
	private PowerDistributionPanel pdp;

	SendableChooser<ConstantMap.AutoChoices> chooser = new SendableChooser<>();
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	GripPipeline grip;
	private double centerX = 0.0;
	private final Object imgLock = new Object();
	public double[] visionImages = new double[100];
	public double[] visionTime = new double[100];
	public int index = 0;
	public double start;
	public boolean logData;

	/*
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	@Override
	public void robotInit()
	{
		chooser.addObject("DRIVE STRAIGHT THEN TURN", ConstantMap.AutoChoices.DRIVE_STRAIGHT_THEN_TURN);
		chooser.addObject("TURN LEFT", ConstantMap.AutoChoices.TURN_LEFT);
		chooser.addObject("TURN RIGHT", ConstantMap.AutoChoices.TURN_RIGHT);	
		chooser.addObject("DO NOTHING", ConstantMap.AutoChoices.DO_NOTHING);
		chooser.addObject("CENTER GEAR PLACEMENT", AutoChoices.CENTER_GEAR_PLACEMENT);
		chooser.addDefault("BASE LINE",
				ConstantMap.AutoChoices.BASE_LINE_TIME_SENSOR);
		chooser.addObject("LEFT SIDE BLUE GEAR",
				ConstantMap.AutoChoices.VISION_GEAR_LEFT);
		chooser.addObject("RIGHT SIDE RED GEAR",
				ConstantMap.AutoChoices.VISION_GEAR_RIGHT);
		chooser.addObject("CENTER",
				ConstantMap.AutoChoices.GEAR_PLACEMENT_DREAM);
		chooser.addObject("GEAR AND FUEL PLACEMENT LEFT",
				ConstantMap.AutoChoices.GEAR_AND_FUEL_PLACEMENT_LEFT);
		chooser.addObject("CALIBRATION",
				ConstantMap.AutoChoices.DRIVE_TO_DA_PIN);
		chooser.addObject("GEAR AND FUEL PLACEMENT RIGHT",
				ConstantMap.AutoChoices.GEAR_AND_FUEL_PLACEMENT_RIGHT);
		SmartDashboard.putData("Auto choices", chooser);

		joystick = new Joystick(RobotMap.RIGHT_SIDE_JOYSTICK_ONE);
		pdp = new PowerDistributionPanel();
		fuel = new Fuel(joystick, pdp);

		gear = new Gear(joystick);
		drive = new Drive(joystick, gear, fuel);
		climb = new Climb(joystick, pdp);

		// pan = new Servo(3);
		// tilt = new Servo(4);
		// pan.setAngle(110);
		// tilt.setAngle(140);
		SmartDashboard.putNumber("CenterX", 0);

		grip = new GripPipeline();

		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		UsbCamera cam2 = CameraServer.getInstance().startAutomaticCapture();
		// CameraServer.getInstance().startAutomaticCapture("cam4", 1);
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

		visionThread = new VisionThread(cam2, grip, grip ->
		{
			if (!grip.filterContoursOutput().isEmpty())
			{
				ArrayList<MatOfPoint> contours = grip.filterContoursOutput();
				ArrayList<MatOfPoint> targets = new ArrayList<MatOfPoint>();
				for (MatOfPoint point : contours)
				{
					double expectedRation = 2.54;
					double tolerance = 2;
					Rect r = Imgproc.boundingRect(point);
					double ration = r.height / r.width;

					if (ration < expectedRation + tolerance
							&& ration > expectedRation - tolerance)
					{
						targets.add(point);
					}
				}

				if (targets.size() == 2)
				{
					Rect r = Imgproc
							.boundingRect(grip.filterContoursOutput().get(0));

					Rect q = Imgproc
							.boundingRect(grip.filterContoursOutput().get(1));
					synchronized (imgLock)
					{
						centerX = (r.x + (r.width / 2) + q.x + (q.width / 2))
								/ 2.0;

					}
					if (logData)
					{
						visionImages[index] = centerX;
						visionTime[index] = Timer.getFPGATimestamp();

						if (index < 100)
						{
							index++;
							System.out.println("Image: " + centerX + " Time: "
									+ Timer.getFPGATimestamp());
						}
						else
						{
							index = 0;
						}
					}
				}
				SmartDashboard.putNumber("CenterX", centerX);

			}
		});

		visionThread.start();

	}

	@Override
	public void autonomousInit()
	{
		logData = true;
		choisir = chooser.getSelected();
		drive.autonomousInit(choisir);

		if (choisir == AutoChoices.BASE_LINE_TIME_SENSOR)
		{
			logData = false;
			System.out.println("Driving to the Base Line");
		}
		if (choisir == AutoChoices.DO_NOTHING)
		{
			logData = false;
			// System.out.println("No Choosing 4 u");

		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic()
	{
		// System.out.println("You have reached autonomousPeriodic");
		drive.autonomousPeriodic(gear);

	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		logData = false;
		// Calling the code from the drive class
		drive.teleopPeriodic();
		climb.teleopPeriodic();
		// fuel.teleopPeriodic();
		gear.teleopPeriodic(fuel);

		// fuel.teleopPeriodic();

		double centerX;
		synchronized (imgLock)
		{
			centerX = this.centerX;
		}

		fuel.fuelCollectLoadingStation();

		if (joystick.getRawButton(7))
		{
			fuel.dischargeFuel();
		}
		else
		{
			fuel.fuelFurnaceRollerSet(0);
		}

		fuel.collectFuelFloor();

		// if (joystick.getPOV() == 270 && panAngle < 150) {
		//
		// // panAngle += 1;
		//
		// } else if (joystick.getPOV() == 0 && tiltAngle > 30) {
		//
		// // tiltAngle -= 1;
		//
		// } else if (joystick.getPOV() == 90 && panAngle > 30) {
		//
		// // panAngle -= 1;
		//
		// } else if (joystick.getPOV() == 180 && tiltAngle < 150) {
		//
		// //tiltAngle += 1;
		//
		// }

		// pan.setAngle(panAngle);
		// tilt.setAngle(tiltAngle);

		SmartDashboard.putNumber("POV VALUE", joystick.getPOV());

		// pan.setAngle(90);
		// tilt.setAngle(90);
		SmartDashboard.putNumber("CenterX", centerX);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic()
	{
		LiveWindow.run();
	}
}