package FP;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FiniteStateController extends FYPBot{

    @Override
    protected void selectSate() {
        fsc.setState(State.DEFEND);
        switch (fsc.getState()){
            case SEARCH:
                System.out.println("SEARCH");
                search();
                break;
            case DEFEND:
                System.out.println("DEFEND");
                defend();
                break;
            case ATTACK:
                System.out.println("ATTACK");
                attack(100, 0.5);
                break;
            case RAM:
                System.out.println("RAM");
                ram();
                break;
        }
    }

    @Override
    protected void ram() {
        attack(10, 3);
        turnRight(180);
    }

    @Override
    protected void attack(int i, double v) {
        ahead(i);
        fire(v);
    }

    @Override
    protected void defend() {
        turnRight(normalRelativeAngleDegrees(1- getHeading()));
        back(1000);
    }

    @Override
    protected void search() {
        ahead(100);
        turnRight(45);
        ahead(100);
        turnRight(-45);
    }
}
