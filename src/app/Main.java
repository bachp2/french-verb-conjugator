package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        //new Program();
        //new Thread(() -> Platform.runLater(() -> new Program())).start();

        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("french verb conjugator");

        Scene scene = new Scene(root, 470, 208);
        scene.getStylesheets().add("stylesheets/comboBoxDisabled.css");
        scene.getStylesheets().add("stylesheets/customButton.css");

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setMinWidth(470);
        primaryStage.setMinHeight(208);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
