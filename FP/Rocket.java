package FP;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import java.util.Random;

public class Rocket extends FYPBot {

    Random r=new Random();
    // to ram , turn to face the enmay and then drive fared at full force
    @Override
    protected void ram(ScannedRobotEvent e) {
        sc.printStateChange(sc.getState(),State.RAM);
        sc.setState(State.RAM);
        ahead(e.getDistance()+10 *moveDirection);
        setFire(1);
    }

    @Override
    protected void attack(ScannedRobotEvent e) {
        int dist=r.nextInt(101);
        double gunTurnAmt;
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

        //if its a senty robot then ignore it, since we should be figting senty robots
        if(e.isSentryRobot()){
            return;
        }

        if(getEnergy()<20){
            sc.printStateChange(sc.getState(),State.DEFEND);
            sc.setState(State.DEFEND);
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);
            setTurnGunRightRadians(gunTurnAmt);
            setTurnLeft(-90-e.getBearing());
            setBack(dist*moveDirection);
            setFire(3);
        }
        else{
            sc.printStateChange(sc.getState(),State.ATTACK);
            sc.setState(State.ATTACK);
            //this an implantation of the 'SuperTracker' form the robocode wiki
            if (e.getDistance() >120) {
                // roate the gun such that it will get where the oppont was last and assueme it will continue in the same direction
                gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/20);
                setTurnGunRightRadians(gunTurnAmt); //turn gun
                setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));
                setAhead((e.getDistance() - dist)*moveDirection);//move forward
                setFire(1.5);
            }
            else{
                //turn at less of an angle because the oppontant is closer
                gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/10);
                setTurnGunRightRadians(gunTurnAmt);//turn gun
                if(e.getDistance()<=40){
                    ram(e);
                }
                else{
                    setTurnLeft(-90-e.getBearing());
                    setAhead((e.getDistance() - dist)*moveDirection);//move forward
                    setFire(3);
                }
            }
        }
    }

    //find out where the bullet came form, turn and move out of the way
    @Override
    protected void defend(HitByBulletEvent e) {
        sc.printStateChange(sc.getState(),State.DEFEND);
        sc.setState(State.DEFEND);
        double energy=getEnergy();
        double bearing=e.getBearing();
        setTurnRight(bearing + 90);
        // strafe by changing direction every 10 ticks
        if (getTime() % 15 == 0) {
            turnDirection();
            setAhead(100 * moveDirection);
        }
        if(energy<40){
            setTurnLeft(-90-bearing);// not 100% accurate but it will allow my robot to escape
            setBack(100 * moveDirection);
        }
        else{
            setTurnLeft(-45-bearing);
            setAhead(100 * moveDirection);

        }

    }

    //move arround while conrlty scannign the area
    @Override
    protected void search() {
        sc.printStateChange(sc.getState(),State.SEARCH);
        sc.setState(State.SEARCH);
        if(!isScanned){
            setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
            ahead(100 *moveDirection);
            turnRight(45);
            ahead(100 *moveDirection);
            turnRight(-45);
        }
        isScanned=false;

    }

}
