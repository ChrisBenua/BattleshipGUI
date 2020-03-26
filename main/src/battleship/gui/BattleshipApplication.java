package battleship.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class BattleshipApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX primary Stage: Hello World!");

//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(event -> System.out.println(Thread.currentThread() + ": Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(new OceanGridPane(300, 300));

        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }
}
