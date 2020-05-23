package simulator.model;

import simulator.model.exceptions.IllegalEnvironmentException;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.SimulatorErrorException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Simulator {

    private File dataFile;
    private Environment environment;
    private int currentDayCount = 0;

    public Simulator() {
        System.out.println("SIMULATOR: Attempting init simulation...");
        try {
            initDataFile();
            environment = new Environment(30,1,2,5);
            System.out.println("SIMULATOR: -> Simulation init successful");
            logOrgCountData();
            currentDayCount++;
        } catch (IllegalEnvironmentException e) {
            e.printStackTrace();
            System.out.println("SIMULATOR: Init simulation FAILED");
        }
    }

    public void progressSimulation() {
        System.out.println("SIMULATOR: Continue simulation day " + currentDayCount + "...");
        try {
            environment.progressEnvironment();
            //environment.printDomain();
        } catch (IllegalEnvironmentException | InvalidIdentifierException | InvalidPositionException e) {
            e.printStackTrace();
        } catch (SimulatorErrorException e) {
            System.out.println("SIMULATOR: Simulation day " + currentDayCount + " ended");
            logOrgCountData();
            currentDayCount++;

        }
    }

    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        return environment.getGridDataString(pos);
    }

    private void initDataFile() {
        dataFile = new File("C:/Users/AlexanderPatrick/IdeaProjects/Pers_Life-Simulator/files/data.csv");
        dataFile.delete();
        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataFile.deleteOnExit();
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
