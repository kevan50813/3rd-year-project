package FP;
import robocode.*;
import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * FYPBot - a robot by Kevan Jordan
 */
public abstract class FYPBot extends AdvancedRobot{
	// Initialization of the robot should be put here
	sateController sc =new sateController();
	protected int moveDirection=1;
	/**
	 * run: FYPBot's default behavior.
	 */
	public void run() {
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// when the robot is turning disable the radar and gun truing to prevent an accrucy loss
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		//setColors
		setBodyColor(new Color(82,12,122));
		setGunColor(new Color(82, 12, 122));
		setRadarColor(new Color(0, 5, 57));
		setBulletColor(new Color(255, 251, 5));
		setScanColor(new Color(158,21,21));
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
		// Robot main loop
		while(true) {
			search();
		}
	}


	//functions for each of the states this will control the states behavior
	protected abstract void ram();

	protected abstract void attack(ScannedRobotEvent e);

	protected abstract void defend(HitByBulletEvent e);

	protected abstract void search();


	/**
	 * onScannedRobot: if it has the energy it will attmpt to engage else it will flee
	 * when it engages it will lock on to the opponat 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energy=getEnergy();
	    if(energy>=20){
			attack(e);
		}
	    else{
			setAhead(100 * moveDirection);
		}
	}


	/**
	 * onHitByBullet: if it has the energy it will turn to face off agist its enmy else it will flee
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		double energy=getEnergy();
		double bearing=e.getBearing();
		System.out.println("i was hit at  "+ bearing+ " Energy: "+energy);
		defend(e);
	}
	
	/**
	 * onHitWall:turn away form the wall then move away form it
	 */
	public void onHitWall(HitWallEvent e) {
			if(sc.getState()==State.DEFEND){
				turnRight(90 * moveDirection);
			}
			else{
				moveDirection= -moveDirection;
			}
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
