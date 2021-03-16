package FP;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FiniteStateController extends FYPBot{

    @Override
    protected void selectSate() {
        System.out.println("i am in Sate "+ sc.getState());
        switch (sc.getState()){
            case SEARCH:
                search();
                break;
            case DEFEND:
                defend();
                break;
            case ATTACK:
                attack(100, 0.5);
                break;
            case RAM:

                ram();
                break;
        }
    }

    @Override
    protected void ram() {
        attack(10, 3);
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
