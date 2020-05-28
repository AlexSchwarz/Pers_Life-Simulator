package simulator.model;

import simulator.model.exceptions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Simulator {

    private File dataFile;
    private Environment environment;

    public Simulator() {
        System.out.println("SIMULATOR: Attempting init simulation...");
        try {
            //initDataFile();
            environment = new Environment(Config.SIZE,Config.PLANT_COUNT,Config.HERBIVORE_COUNT,Config.CARNIVORE_COUNT);
            System.out.println("SIMULATOR: -> Simulation init successful");
            //logOrgCountData();
        } catch (IllegalEnvironmentException | SimulatorErrorException e) {
            e.printStackTrace();
            System.out.println("SIMULATOR: Init simulation FAILED");
        }
    }

    public void progressSimulation() {
        System.out.println("SIMULATOR: Progress simulation");
        try {
            environment.progressCurrentOrganism();
        } catch (InvalidIdentifierException e) {
            e.printStackTrace();
        } catch (SimulatorErrorException e) {
            e.printStackTrace();
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        //logOrgCountData();
        //currentDayCount++;
    }

    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        return environment.getGridDataString(pos);
    }

    public String getCurrentOrgDataString() {
        return environment.getCurrentOrganismDataString();
    }

    private void initDataFile() {
        dataFile = new File("C:/Users/AlexanderPatrick/IdeaProjects/Pers_Life-Simulator/files/data.csv");
        dataFile.delete();
        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //dataFile.deleteOnExit();
    }

    /*
    private void logOrgCountData() {
        System.out.println("SIMULATOR: Attempting log organism count...");
        String data = "DAY;" + currentDayCount + ";" + environment.getEnvironmentDataString();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile, true))) {
            bw.write(data);
            bw.newLine();
            System.out.println("SIMULATOR: -> Log successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}
