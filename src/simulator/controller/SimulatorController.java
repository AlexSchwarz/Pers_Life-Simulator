package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import simulator.model.PositionVector;
import simulator.model.Simulator;
import simulator.model.exceptions.InvalidPositionException;

public class SimulatorController {
    @FXML GridPane GridPaneDomain;
    @FXML Button ButtonInitSim;
    @FXML Button ButtonStepSim;
    Simulator simulator;
    Label[][] labelArray = new Label[30][30];

    @FXML
    public void initialize() {
        setEmptyArray();
    }

    private void setEmptyArray() {
        for(int i = 0; i < 30; i++) {
            for(int j = 0; j < 30; j++) {
                labelArray[i][j] = new Label("-");
            }
        }
    }

    private void updateArray() throws InvalidPositionException {
        for(int i = 0; i < 30; i++) {
            for(int j = 0; j < 30; j++) {
                labelArray[i][j] = new Label(simulator.getGridDataString(new PositionVector(j,i)));
            }
        }
    }

    @FXML
    public void initSim() throws InvalidPositionException {
        simulator = new Simulator();
        updateDomain();
    }

    @FXML
    public void stepSim() throws InvalidPositionException {
        simulator.progressSimulation();
        updateDomain();
    }

    private void updateDomain() throws InvalidPositionException {
        removeLables();
        updateArray();
        for(int i = 0; i < 30; i++) {
            for(int j = 0; j < 30; j++) {
                GridPaneDomain.add(labelArray[i][j], i, j);
            }
        }
    }

    private void removeLables() {
        for(int i = 0; i < 30; i++) {
            for(int j = 0; j < 30; j++) {
                GridPaneDomain.getChildren().remove(labelArray[i][j]);
            }
        }
    }
}
