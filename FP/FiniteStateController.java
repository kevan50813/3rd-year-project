package FP;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class FiniteStateController extends FYPBot{


    @Override
    protected void ram() {
        sc.setState(State.RAM);
        setTurnRight(moveDirection*5);
        ahead(10);
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

        if (e.getDistance() >=150) {
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/40);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt); //turn gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));//drive towards the enemies predicted future location
            setAhead((e.getDistance() - 100)*moveDirection);//move forward
            if(e.getDistance()<=10){
                ram();
            }
            else{
                setFire(1.5);//fire
            }

        }
        else{
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt);//turn gun
            setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            setAhead((e.getDistance() - 100)*moveDirection);//move forward
            if(e.getDistance()<=10){
                ram();
            }
            else{
                setFire(3);
            }
        }

    }

    @Override
    protected void defend(HitByBulletEvent e) {
        sc.setState(State.DEFEND);
        double bearing=e.getBearing();
        turnRight(-bearing); // not 100% accutate but it will allow my robot to escape
        setAhead(100 * moveDirection);
    }

    @Override
    protected void search() {
        sc.setState(State.SEARCH);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        ahead(100);
        turnRight(45);
        ahead(100);
        turnRight(-45);
    }

}
