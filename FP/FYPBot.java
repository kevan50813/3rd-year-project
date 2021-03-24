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
	protected boolean isScanned=false;
	/**
	 * run: FYPBot's default behavior.
	 */
	public void run() {
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// when the robot is turning disable the radar and gun truing to prevent an accuracy loss
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


	//functions for each of the states this will control the states behavior, having them as abstrct classes means that myu code is polymprphic due to baing able to use the same code acorss mutiple bots
	protected abstract void ram(ScannedRobotEvent e);

	protected abstract void attack(ScannedRobotEvent e);

	protected abstract void defend(HitByBulletEvent e);

	protected abstract void search();


	/**
	 * onScannedRobot: if it has the energy it will attempt to engage else it will flee
	 * when it engages it will lock on to the opponent
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energy=getEnergy();
		if(!isScanned){
			isScanned=true;
			System.out.println("i have locacted "+ e.getName());
		}
	    if(energy>=20){
			attack(e);
		}
	    else{
			setAhead(100 * moveDirection);
		}
	}


	/**
	 * onHitByBullet: if it has the energy it will turn to face off against its enemy's else it will flee
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
				moveDirection= moveDirection * -1;
				setTurnLeft(45);
				setAhead(100*moveDirection);
	}	
	
	/**
	 * OnWin: do a victory dance
	 */
	 public void onWin(WinEvent e){
	 	System.out.println("i won this round");
		turnRight(180);
		turnRight(-180);
		turnRight(360);

	 }

}
