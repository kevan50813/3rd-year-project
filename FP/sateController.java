package FP;

public class sateController {


    private  State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void printStateChange(State lastState, State newState){
        if(lastState==newState || lastState==null){
            return;
        }
        System.out.println("Changed to Sate "+newState);
    }



}
