package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2228.robot.ConstantMap.AutoChoices;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

	/*
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	@Override
	public void robotInit()
	{
		// chooser.addDefault("Default Auto", defaultAuto);
		// chooser.addObject("My Auto", customAuto);
		chooser.addDefault("Do Nothing", ConstantMap.AutoChoices.DO_NOTHING);
		chooser.addObject("Base Line",
				ConstantMap.AutoChoices.BASE_LINE_TIME_SENSOR);
		chooser.addObject("Left Gear Placement",
				ConstantMap.AutoChoices.LEFT_AUTO_GEAR_PLACEMENT);
		SmartDashboard.putData("Auto choices", chooser);

		joystick = new Joystick(RobotMap.RIGHT_SIDE_JOYSTICK_ONE);
		pdp = new PowerDistributionPanel();
		fuel = new Fuel(joystick, pdp);
		drive = new Drive();
		climb = new Climb(drive.getJoystick());
		gear = new Gear(drive.getJoystick());
		// shooter = new CANTalon(RobotMap.RIGHT_SHOOTER_ONE);
		// SmartDashboard.putString("autonomous selection",
		// ConstantMap.doNothing);

		SmartDashboard.putNumber("CenterX", 0);
		
		grip = new GripPipeline();
		
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		
		
		visionThread = new VisionThread(camera,grip, grip -> {
				if(!grip.filterContoursOutput().isEmpty()){
					ArrayList<MatOfPoint> contours = grip.filterContoursOutput();
					ArrayList<MatOfPoint> targets = new ArrayList<MatOfPoint>();
					for(MatOfPoint point : contours){
						double expectedRation = 2.54;
						double tolerance = 2;
						Rect r = Imgproc.boundingRect(point);
						double ration = r.height/r.width;
						
						if(ration < expectedRation + tolerance && ration > expectedRation - tolerance){
							targets.add(point);
						}
					}
						
					if(targets.size() == 2){
						Rect r = Imgproc.boundingRect(grip.filterContoursOutput().get(0));
						
						Rect q = Imgproc.boundingRect(grip.filterContoursOutput().get(1));
						synchronized(imgLock){
							centerX = (r.x + (r.width/2) + q.x + (q.width/2))/2.0;
					}
				}
				SmartDashboard.putNumber("CenterX", centerX);
			}
		});
			visionThread.start();
		
				

	}

	/*
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
	public void autonomousInit()
	{
		choisir = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		// autoSelected = SmartDashboard.getString("autonomous selection",
		// ConstantMap.doNothing);
		drive.autonomousInit(choisir);

		// System.out.println(autoSelected);

		if (choisir == AutoChoices.DO_NOTHING)
		{
//			System.out.println("No Choosing 4 u");
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
		/*
		 * // Put custom auto code here break; case defaultAuto: default: // Put
		 * default auto code here break; }
		 */
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		// Calling the code from the drive class
		drive.teleopPeriodic();
		climb.teleopPeriodic();
		gear.teleopPeriodic();

		fuel.teleopPeriodic();
		
		double centerX;
		synchronized (imgLock){
			centerX = this.centerX;
		}
		
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
