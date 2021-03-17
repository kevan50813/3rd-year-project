package FP;

import robocode.ScannedRobotEvent;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FiniteStateController extends FYPBot{


    @Override
    protected void ram() {
        System.out.println("i am in Sate "+ sc.getState());
        ahead(100);
        setFire(1);
    }

    @Override
    protected void attack(ScannedRobotEvent e) {
        System.out.println("i am in Sate "+ sc.getState());
        double energy=getEnergy();
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        double  velocity=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        double gunTurnAmt=0.0;
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

        //this an implantation of the 'SuperTracker' form the robocode wiki
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);//randomly change speed
        }
        super.turnRadar(e,energy,absBearing,velocity,gunTurnAmt);
    }

    @Override
    protected void defend(ScannedRobotEvent e) {
        System.out.println("i am in Sate "+ sc.getState());
        setBack((e.getDistance() - 140));
        setFire(0.1);
    }

    @Override
    protected void search() {
        System.out.println("i am in Sate "+ sc.getState());
        ahead(100);
        turnRight(45);
        ahead(100);
        turnRight(-45);
    }
}
