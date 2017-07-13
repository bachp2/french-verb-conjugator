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
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("french verb conjugator");
        new Thread(new Runnable() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        new Program();
                    }
                });
            }
        }).start();
        Scene scene = new Scene(root, 470, 208);
        scene.getStylesheets().add("stylesheets/comboBoxDisabled.css");
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
