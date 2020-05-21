package controller;

import model.Simulator;
import model.exceptions.InvalidPositionException;

public class Main {

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        try {
            simulator.runSimulation();
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }
}
