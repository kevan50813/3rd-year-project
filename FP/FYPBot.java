package FP;
import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * FYPBot - a robot by Kevan Jordan
 */
public abstract class FYPBot extends AdvancedRobot{
	// Initialization of the robot should be put here
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
		sc.setState(State.SEARCH);
		// Robot main loop
		while(true) {
			scan();
			System.out.println("i am in Sate "+ sc.getState());
			selectSate();
		}
	}





	//use the Finite ste contoler to get the currnat state , whitch will be siwtched as the robot perfomes cations such as OnScan
	protected abstract void selectSate();

	//functions for each of the states this will control the states behavoiure
	protected abstract void ram();

	protected abstract void attack(int i, double v);

	protected abstract void defend();

	protected abstract void search();


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
		//this an implantation of the 'SuperTracker' form the robocode wiki
		if (e.getDistance() >=100) {
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);
			turnRadar(e, energy, absBearing, velocity, gunTurnAmt);
		}
		if(e.getDistance()>=50&&e.getDistance()<100){
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/100);
			turnRadar(e, energy, absBearing, velocity, gunTurnAmt);
		}
		else{
			if(energy>=50){
				sc.setState(State.RAM);
			}
		}
	}

	private void turnRadar(ScannedRobotEvent e, double energy, double absBearing, double velocity, double gunTurnAmt) {
		setTurnGunRightRadians(gunTurnAmt);
		setTurnRightRadians(Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));
		if(energy>=30){
			setAhead((e.getDistance() - 140));
			if(e.getDistance()<20){
				setFire(1);
			}
			else{
				setFire(2.5);
			}
			sc.setState(State.ATTACK);
		}
		else{
			setBack((e.getDistance() - 140));
			setFire(0.1);
			sc.setState(State.DEFEND);
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
			sc.setState(State.DEFEND);
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
