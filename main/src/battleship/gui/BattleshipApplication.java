package battleship.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class BattleshipApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX primary Stage: Hello World!");

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println(Thread.currentThread() + ": Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
