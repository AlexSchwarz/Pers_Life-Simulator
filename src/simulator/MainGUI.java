package simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private Stage primaryStage;

    @Override
    public void start (Stage primaryStage){
        this.primaryStage = primaryStage;
        mainWindow();
    }

    private void mainWindow () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/fxml/MainWindow.fxml"));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);

            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(350);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
