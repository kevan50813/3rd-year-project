package FP;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class FiniteStateController extends FYPBot{


    @Override
    protected void ram(ScannedRobotEvent e) {
        sc.setState(State.RAM);
        System.out.println("i am in Sate "+ sc.getState());
        ahead(e.getDistance() + 10);
        setFire(1);
    }

    @Override
    protected void attack(ScannedRobotEvent e) {
        sc.setState(State.ATTACK);
        System.out.println("i am in Sate "+ sc.getState());
        double energy=getEnergy();
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

        //this an implantation of the 'SuperTracker' form the robocode wiki
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);//randomly change speed
        }
        //lock on to its target
        super.turnRadar(e,absBearing,velocity);
        if(e.getDistance()<15 && energy >20){
            ram(e);
        }
    }

    @Override
    protected void defend(HitByBulletEvent e) {
        sc.setState(State.DEFEND);
        System.out.println("i am in Sate "+ sc.getState());
        setBack(100);
        setFire(0.5);
    }

    @Override
    protected void search() {
        sc.setState(State.SEARCH);
        System.out.println("i am in Sate "+ sc.getState());
        ahead(100);
        turnRight(45);
        ahead(100);
        turnRight(-45);
    }
}
