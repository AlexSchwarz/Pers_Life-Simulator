package simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import simulator.model.DomainReadable;
import simulator.model.DomainWritable;
import simulator.model.PositionVector;
import simulator.model.exceptions.IllegalEnvironmentException;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.SimulatorErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main  {

    //private Stage primaryStage;

    public static void main(String[] args) {
        try {
            DomainWritable domain = new DomainWritable(30);
            domain.setIDAtPosition("1P", new PositionVector(15,16));
            domain.setIDAtPosition("2C", new PositionVector(17,15));
            domain.setIDAtPosition("3C", new PositionVector(18,15));
            domain.setIDAtPosition("4H", new PositionVector(19,15));

            System.out.println(domain.getClosestOccurrenceOfStringInProximity(new PositionVector(15,15), "---", 8));
            System.out.println(domain.getClosestOccurrenceOfStringInProximity(new PositionVector(20,15), "C", 8));


            //domain.printDomain();
        } catch (SimulatorErrorException | InvalidPositionException e) {
            e.printStackTrace();
        }
    }
    /*

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    private void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/MainWindow.fxml"));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);

            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(350);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    *
 */


}
