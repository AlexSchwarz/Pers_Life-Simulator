package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import simulator.model.Config;
import simulator.model.PositionVector;
import simulator.model.Simulator;
import simulator.model.exceptions.EnvironmentCycleCompleteException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.NoOrganismsLeftException;

import java.util.concurrent.TimeUnit;

public class SimulatorController {
    @FXML GridPane GridPaneDomain;
    @FXML Button ButtonInitSim;
    @FXML Button ButtonStepSim;
    @FXML Button ButtonRunDay;
    Simulator simulator;
    Label[][] labelArray = new Label[Config.SIZE][Config.SIZE];

    @FXML
    public void initialize() {
        setEmptyArray();
        updateGridPane();
        ButtonStepSim.setDisable(true);
        ButtonRunDay.setDisable(true);
    }

    private void setEmptyArray() {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                Label label = new Label("-");
                label.setFont(new Font("Arial", 11));
                labelArray[i][j] = label;
            }
        }
    }

    @FXML
    public void initSim() throws InvalidPositionException {
        ButtonStepSim.setDisable(false);
        ButtonRunDay.setDisable(false);
        simulator = new Simulator();
        removeLables();
        updateArray();
        updateGridPane();
    }

    @FXML
    public void stepSim() throws InvalidPositionException {
        try {
            simulator.progressSimulation();
        } catch (EnvironmentCycleCompleteException | NoOrganismsLeftException e) {

        }
        removeLables();
        updateArray();
        updateGridPane();
    }

    @FXML
    public void runDay() throws InvalidPositionException {
        boolean run = true;
        while(run) {
            try {
                simulator.progressSimulation();
            } catch (EnvironmentCycleCompleteException | NoOrganismsLeftException e) {
                run = false;
            }
            removeLables();
            updateArray();
            updateGridPane();
        }
    }

    private void updateArray() throws InvalidPositionException {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                String dataString = simulator.getGridDataString(new PositionVector(j,i));
                Label label = new Label(dataString);
                label.setFont(new Font("Arial", 11));
                if(dataString.equals(simulator.getCurrentOrgDataString())) {
                    label.setTextFill(Color.RED);
                }else if(dataString.contains("P")) {
                    label.setTextFill(Color.GREEN);
                }else if (dataString.contains("C")){
                    label.setTextFill(Color.BLACK);
                }else{
                    label.setTextFill(Color.BLUE);
                }
                labelArray[i][j] = label;
            }
        }
    }

    private void updateGridPane() {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                GridPaneDomain.add(labelArray[i][j], i, j);
            }
        }
    }

    private void removeLables() {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                GridPaneDomain.getChildren().remove(labelArray[i][j]);
            }
        }
    }
}
