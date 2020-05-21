package model;

import model.exceptions.InvalidPositionException;

public class Simulator {

    public Simulator() {

    }

    public void runSimulation() throws InvalidPositionException {
        System.out.println("SIMULATOR: Starting simulation...");
        Domain domain = new Domain(10,10,2, 2, 2);
        System.out.println("SIMULATOR: Simulation Ended");
    }
}
