package FP;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import java.util.Random;

public class Rocket extends FYPBot{

    Random r=new Random();
    // to ram , turn to face the enmay and then drive fared at full force
    @Override
    protected void ram(ScannedRobotEvent e) {
        sc.setState(State.RAM);
        System.out.println(sc.getState());
        ahead(e.getDistance()+10 *moveDirection);
        setFire(1);
    }

    @Override
    protected void attack(ScannedRobotEvent e) {
        sc.setState(State.ATTACK);
        int dist=r.nextInt(101);
        double gunTurnAmt;
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        //only print once to prevent the termial form flooding with the workd 'ATTACK'
        if(!printed){
            System.out.println(sc.getState());
            printed=true;
        }

        if(e.isSentryRobot()){
            return;
        }

        //this an implantation of the 'SuperTracker' form the robocode wiki
        if (e.getDistance() >120) {
            // roate the gun such that it will get where the oppont was last and assueme it will continuw in the same direction
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+velocity/25);
            setTurnGunRightRadians(gunTurnAmt); //turn gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+velocity/getVelocity()));
            setAhead((e.getDistance() - dist)*moveDirection);//move forward
            setFire(1.5);
        }
        else{
            //turn at less of an agle baascue the oppontant is claoser
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

    //in the end state , it will : find out where the bullet came form, turn and move out of the way
    @Override
    protected void defend(HitByBulletEvent e) {
        sc.setState(State.DEFEND);
        System.out.println(sc.getState());
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
        System.out.println(sc.getState());
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
