package JavaFX.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // load fxml file
    Parent root = FXMLLoader.load(Objects.requireNonNull(
        getClass().getClassLoader().getResource("View/InGame.fxml")
    ));
    Scene scene = new Scene(root, 1200, 800);
    scene.getStylesheets().add(getClass().getResource("/Style/InGame.css").toExternalForm());


    // title
    primaryStage.setTitle("TURNCOAT");
    primaryStage.setResizable(false);

    // set scene
    primaryStage.setScene(scene);

    // show
    primaryStage.show();
  }
}