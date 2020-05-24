package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import simulator.model.Config;
import simulator.model.PositionVector;
import simulator.model.Simulator;
import simulator.model.exceptions.InvalidPositionException;

public class SimulatorController {
    @FXML GridPane GridPaneDomain;
    @FXML Button ButtonInitSim;
    @FXML Button ButtonStepSim;
    Simulator simulator;
    Label[][] labelArray = new Label[Config.SIZE][Config.SIZE];

    @FXML
    public void initialize() {
        setEmptyArray();
        updateGridPane();
    }

    private void setEmptyArray() {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                labelArray[i][j] = new Label("-");
            }
        }
    }

    @FXML
    public void initSim() throws InvalidPositionException {
        simulator = new Simulator();
        removeLables();
        updateArray();
        updateGridPane();
    }

    @FXML
    public void stepSim() throws InvalidPositionException {
        simulator.progressSimulation();
        removeLables();
        updateArray();
        updateGridPane();
    }

    private void updateArray() throws InvalidPositionException {
        for(int i = 0; i < Config.SIZE; i++) {
            for(int j = 0; j < Config.SIZE; j++) {
                labelArray[i][j] = new Label(simulator.getGridDataString(new PositionVector(j,i)));
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
