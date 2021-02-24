package FYP;
import robocode.*;
import robocode.util.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * FYPBot - a robot by Kevan Jordan
 */
public class FYPBot extends Robot
{
	/**
	 * run: FYPBot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		//setColors
		setBodyColor(new Color(82,12,122));
		setGunColor(new Color(82, 12, 122));
		setRadarColor(new Color(0, 100, 100));
		setBulletColor(new Color(255, 251, 5));
		setScanColor(new Color(158,21,21));

		// Robot main loop
		while(true) {
			ahead(100);
			turnRight(45);
			ahead(100);
			turnRight(-45);
			turnGunRight(360);
			scan();
		}
	}
	
	/**
	 * Ram the opponat if it is close enough else just return
	 * not 100% accurate but will work sometimes
	 */
	public void ram(ScannedRobotEvent e){
	double bearing=e.getBearing();
	double absBearing=getHeading()+bearing;
	double gunBearing=normalRelativeAngleDegrees(absBearing - getGunHeading());
		if(e.getDistance()<=20){
			turnRight(absBearing);
			ahead(100);
			fire(1);
		}
	}	
	

	/**
	 * onScannedRobot: if it has the energy it will attmpt to engage else it will flee
	 * when it engages it will lock on to the opponat 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energy=getEnergy();
		double bearing=e.getBearing();
		double absBearing=getHeading()+bearing;
		double gunBearing=normalRelativeAngleDegrees(absBearing - getGunHeading());
		
		turnGunRight(gunBearing);
		if(energy<40){
			fire(1);
			ahead(300);
		}
		else if(energy>100){
			fire(5);
			ahead(100);
			ram(e);
		}
		else{
			fire(3);
			ahead(200);
			ram(e);
		}
	}

	/**
	 * onHitByBullet: if it has the energy it will turn to face off agist its enmy else it will flee
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		double energy=getEnergy();
		double bearing=e.getBearing();
		if(energy<50){
			turnRight(-bearing);
			ahead(100);
		}
		else{
			turnGunRight(bearing);
			ahead(100);
		}
	}
	
	/**
	 * onHitWall:turn away form the wall then move away form it
	 */
	public void onHitWall(HitWallEvent e) {
			double bearing=e.getBearing();
			turnRight(-bearing);
			ahead(100);
	}	
	
	/**
	 * OnWin: do a vicoty dance
	 */
	 public void onWin(WinEvent e){
		turnRight(180);
		turnRight(-180);
		turnRight(360);
	 }
}
