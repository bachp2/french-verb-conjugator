package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("french verb conjugator");
        Scene scene = new Scene(root, 545, 350);
        scene.getStylesheets().add("App/comboBoxDisabled.css");
        primaryStage.setScene(scene);
        new Program();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
