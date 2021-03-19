package FP;
import robocode.*;
import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * FYPBot - a robot by Kevan Jordan
 */
public abstract class FYPBot extends AdvancedRobot{
	// Initialization of the robot should be put here
	private double gunTurnAmt;
	sateController sc =new sateController();
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


	//functions for each of the states this will control the states behavoiure
	protected abstract void ram(ScannedRobotEvent e);

	protected abstract void attack(ScannedRobotEvent e);

	protected abstract void defend();

	protected abstract void search();


	/**
	 * onScannedRobot: if it has the energy it will attmpt to engage else it will flee
	 * when it engages it will lock on to the opponat 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
	    if(e.getEnergy()>=5){
			attack(e);
		}
		else{
			defend();
		}
	}

	/**
	 * this mthod turns the radar towards the opponent and fires with varing power bsed on the ditscance form its opponrnt
	 */
	protected  void turnRadar(ScannedRobotEvent e, double absBearing, double velocity) {

		//move forward
		if (e.getDistance() >150) {
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/22);//amount to turn our gun, lead just a little bit
			setTurnGunRightRadians(gunTurnAmt); //turn our gun
			setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));//drive towards the enemies predicted future location
			setFire(3);//fire
		}
		else{
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/15);//amount to turn our gun, lead just a little bit
			setTurnGunRightRadians(gunTurnAmt);//turn our gun
			setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
			setFire(1.5);
		}
		setAhead((e.getDistance() - 140));//move forward

	}

	/**
	 * onHitByBullet: if it has the energy it will turn to face off agist its enmy else it will flee
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		double energy=getEnergy();
		double bearing=e.getBearing();
		if(energy<50){
			turnRight(-bearing);
		}
		else{
			turnGunRight(bearing);
		}
		ahead(100);
	}
	
	/**
	 * onHitWall:turn away form the wall then move away form it
	 */
	public void onHitWall(HitWallEvent e) {
			if(sc.getState()==State.DEFEND){
				turnRight(90);
			}
			else{
				turnRight(-e.getBearing());
			}
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
