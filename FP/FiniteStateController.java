package FP;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class FiniteStateController extends FYPBot{

    // to ram , turn to face the enmay and then drive fared at full force
    @Override
    protected void ram() {
        sc.setState(State.RAM);
        ahead(10*moveDirection);
        setFire(1);
    }

    @Override
    protected void attack(ScannedRobotEvent e) {
        sc.setState(State.ATTACK);
        double gunTurnAmt;
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        //this an implantation of the 'SuperTracker' form the robocode wiki
        if (e.getDistance() >120) {
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/40);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt); //turn gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));
            setAhead((e.getDistance() - 100)*moveDirection);//move forward
            setFire(1.5);
        }
        else{
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt);//turn gun
            if(e.getDistance()<=10){
                ram();
            }
            else{
                setTurnLeft(-90-e.getBearing());
                setAhead((e.getDistance() - 100)*moveDirection);//move forward
                setFire(3);
            }
        }

    }

    //in the end state , it will : find out where the bullet came form, turn and move out of the way
    @Override
    protected void defend(HitByBulletEvent e) {
        sc.setState(State.DEFEND);
        double energy=getEnergy();
        double bearing=e.getBearing();
        if(energy<40){
            setTurnLeft(-90-bearing);// not 100% accurate but it will allow my robot to escape
            setBack(100 * moveDirection);
        }
        else{
            setTurnLeft(bearing);
        }
    }

    //move arround while conrlty scannign the area
    @Override
    protected void search() {
        sc.setState(State.SEARCH);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        ahead(100 *moveDirection);
        turnRight(45);
        ahead(100 *moveDirection);
        turnRight(-45);
    }

}
