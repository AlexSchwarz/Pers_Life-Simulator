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
    private int currentDayCount = 0;

    public Simulator() {
        System.out.println("SIMULATOR: Attempting init simulation...");
        try {
            initDataFile();
            environment = new Environment(Config.SIZE,Config.PLANT_COUNT,Config.HERBIVORE_COUNT,Config.CARNIVORE_COUNT);
            System.out.println("SIMULATOR: -> Simulation init successful");
            //logOrgCountData();
            currentDayCount++;
        } catch (IllegalEnvironmentException e) {
            e.printStackTrace();
            System.out.println("SIMULATOR: Init simulation FAILED");
        }
    }

    public void progressSimulation() throws EnvironmentCycleCompleteException, NoOrganismsLeftException {
        System.out.println("SIMULATOR: Continue simulation day " + currentDayCount + "...");
        try {
            environment.progressEnvironmentByOrganism();
        } catch (IllegalEnvironmentException | InvalidIdentifierException | InvalidPositionException e) {
            e.printStackTrace();
        } catch (EnvironmentCycleCompleteException e) {
            System.out.println("SIMULATOR: Simulation day " + currentDayCount + " ended ********************");
            //logOrgCountData();
            currentDayCount++;
            throw new EnvironmentCycleCompleteException("day done");
        }
    }

    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        return environment.getGridDataString(pos);
    }

    public String getCurrentOrgDataString() {
        return environment.getCurrentOrgDataString();
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
}
