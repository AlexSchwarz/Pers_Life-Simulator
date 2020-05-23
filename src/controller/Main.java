package controller;

import model.Simulator;

public class Main {

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        int counter = 0;
        int maxDays = 100;
        while (counter < maxDays) {
            simulator.progressSimulation();
            counter++;
        }
    }
}
