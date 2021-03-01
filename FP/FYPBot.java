package FP;
import robocode.*;
import robocode.util.*;
import java.awt.Color;
import robocode.util.Utils;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * FYPBot - a robot by Kevan Jordan
 */
public class FYPBot extends AdvancedRobot{
	// Initialization of the robot should be put here
	FiniteStateController fsc=new FiniteStateController();

	/**
	 * run: FYPBot's default behavior.
	 */
	public void run() {
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);;
		//setColors
		setBodyColor(new Color(82,12,122));
		setGunColor(new Color(82, 12, 122));
		setRadarColor(new Color(0, 5, 57));
		setBulletColor(new Color(255, 251, 5));
		setScanColor(new Color(158,21,21));
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
		fsc.setState(State.SEARCH);
		// Robot main loop
		while(true) {
			scan();
			switch (fsc.getState()){
				case SEARCH:
					ahead(100);
					turnRight(45);
					ahead(100);
					turnRight(-45);
					break;
				case DEFEND:
					turnGunRight(360);
					break;
				case ATTACK:
					ahead(100);
					fire(0.5);
					break;
			}
		}
	}
	
	/**
	 * Ram the opponat if it is close enough else just return
	 * not 100% accurate but will work sometimes
	 */
	public void ram(ScannedRobotEvent e){ ;
		if(e.getDistance()<=20){
			ahead(100);
			fire(3);
		}
	}	
	

	/**
	 * onScannedRobot: if it has the energy it will attmpt to engage else it will flee
	 * when it engages it will lock on to the opponat 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energy=getEnergy();
		double absBearing=e.getBearingRadians()+getHeadingRadians();
		double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
		double gunTurnAmt;
		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
		//this an implention of the 'SuperTracker' form the robocode wiki
		if (e.getDistance() >=40) {
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);
			setTurnGunRightRadians(gunTurnAmt);
			setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));
			if(energy>=30){
				setAhead((e.getDistance() - 140));
				setFire(1);
				fsc.setState(State.ATTACK);
			}
			else{
				setBack((e.getDistance() - 140));
				setFire(0.1);
				fsc.setState(State.DEFEND);
			}
		}
		else{
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
