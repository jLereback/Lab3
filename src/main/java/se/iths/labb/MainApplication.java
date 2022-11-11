package se.iths.labb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 450);

        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("LereLabb3 Julia Lerebäck Corell");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("JavaDuke.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}