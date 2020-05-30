package simulator;

import simulator.model.Config;
import simulator.model.Simulator;

public class MainConsole {

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runSimulation(Config.MAX_RUN_PROGRESSIONS);
    }
}
