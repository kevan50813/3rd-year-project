package FP;

import robocode.ScannedRobotEvent;

public class FiniteStateController extends FYPBot{

    private double gunTurnAmt;

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
        turnRadar(e,absBearing,velocity);
        if(e.getDistance()<15 && energy >20){
            ram(e);
        }
    }

    @Override
    protected void defend() {
        sc.setState(State.DEFEND);
        System.out.println("i am in Sate "+ sc.getState());
        setBack(100);
        setFire(0.5);
    }

    @Override
    protected void search() {
        sc.setState(State.SEARCH);
        System.out.println("i am in Sate "+ sc.getState());
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        ahead(100);
        turnRight(45);
        ahead(100);
        turnRight(-45);
    }

    /**
     * this mthod turns the radar towards the opponent and fires with varing power bsed on the ditscance form its opponrnt
     */
    private void turnRadar(ScannedRobotEvent e, double absBearing, double velocity) {

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
}
