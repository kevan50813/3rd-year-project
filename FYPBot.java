package FYP;
import robocode.*;
import robocode.util.*;
import java.awt.Color;

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
		setBodyColor(new Color(5, 165, 240));
		setGunColor(new Color(0, 150, 50));
		setRadarColor(new Color(0, 100, 100));
		setBulletColor(new Color(255, 251, 5));
		setScanColor(new Color(158,21,21));

		// Robot main loop
		while(true) {
			ahead(100);
			turnGunRight(360);
			scan();
		}
	}
	/**
	 * normlaise the angle to a relative angle between 179 and -180 
	 */
	public double normaliseAngle(double angle){
		return(angle%=360)>=0?(angle<180)?angle:angle-360:(angle>= -180)?angle:angle+360;
	}

	/**
	 * onScannedRobot: if it has the energy it will attmpt to engage else it will flee
	 * when it engages it will lock on to the opponat 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energy=getEnergy();
		double bearing=e.getBearing();
		double absBearing=getHeading()+bearing;
		double gunBearing=normaliseAngle(absBearing-getGunHeading());
		
		turnGunRight(gunBearing);
		if(energy<50){
			fire(1);
			back(100);
		}
		else{
			fire(2);
			ahead(100);
		}
		scan();
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
			turnGunRight(360);
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
	 public void onWin(WinEvent e){
		turnRight(180);
		turnRight(-180);
		turnRight(360);
	 }
}
