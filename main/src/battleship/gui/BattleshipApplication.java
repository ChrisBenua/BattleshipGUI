package battleship.gui;

import battleship.guiservices.Assembly;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Main class of JavaFX application
 */
public class BattleshipApplication extends Application implements IEndGameReactor {

    private Assembly assembly = new Assembly();
    private battleship.network.Assembly networkAssembly = new battleship.network.Assembly();
    private Stage primaryStage;
    private boolean isCoordsEnterStageShown = false;

    /**
     * Generates Scene and starts game initializing it with Assembly instance
     * @return new Scene
     */
    private Scene startGame() {
        primaryStage.setTitle("Battleship Game");

        var battleshipGame = assembly.getGame();
        var logger = assembly.getEventsLogger();

        var eventsLogContainer = new EventLogsContainer();
        var statsContainer = new GameStatsContainer();
        logger.subscribe(eventsLogContainer);

        battleshipGame.placeShipsRandomly();
        RootPane root = new RootPane(battleshipGame);
        root.setApplication(this);
        battleshipGame.setSubscriber(root);
        var oceanGridPane = new OceanGridPane(battleshipGame.getOceanState(),
                assembly.getOceanCellStateColorMapper(), assembly.getOceanCellEventHandlerFactory(), root);

        root.setChildren(oceanGridPane, eventsLogContainer, statsContainer);

        var scene = new Scene(root, 500, 500);
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(520);
        scene.getStylesheets().add(getClass().getResource("text-area.css").toExternalForm());

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!isCoordsEnterStageShown) {
                    isCoordsEnterStageShown = true;
                    System.out.println("DETECTED");
                    Scene coordsScene = new Scene(new EnterCoordinatesPane((row, col) -> {
                        this.assembly.getGame().shootAt(row, col);
                        root.refresh(RootPane.RefreshValues.ALL);
                    }));
                    Stage coordsStage = new Stage();
                    coordsStage.setMinWidth(200);
                    coordsStage.setMinHeight(120);
                    coordsStage.setScene(coordsScene);
                    coordsStage.show();
                    coordsStage.setOnHidden(event -> {
                        isCoordsEnterStageShown = false;
                    });
                }
            }
        });

        return scene;
    }

    /**
     * Entry function for JavaFX app
     * @param primaryStage window of current JavaFX app
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        var params = getParameters().getRaw();
        String mode = params.size() > 0 ? params.get(0).toLowerCase() : "server";
        if (mode.equals("server")) {
            primaryStage.setScene(startServer());
        } else {
            primaryStage.setScene(startClient());
        }
        //primaryStage.setScene(startGame());
        primaryStage.show();
    }

    public Scene startServer() {
        primaryStage.setTitle("Battleship Game Server");

        var serverStartPane = new ServerStartPane(assembly.getGame(), networkAssembly);
        var scene = new Scene(serverStartPane, 500, 200);
        primaryStage.setMinHeight(100);
        primaryStage.setMinWidth(200);

        return scene;
    }

    public Scene startClient() {
        primaryStage.setTitle("Battleship Game Client");

        var serverStartPane = new ClientStartPane(assembly.getGame(), networkAssembly);
        var scene = new Scene(serverStartPane, 500, 300);
        primaryStage.setMinHeight(150);
        primaryStage.setMinWidth(200);

        return scene;
    }

    /**
     * Event handler for restarting game
     */
    @Override
    public void restart() {
        Platform.runLater(() -> {
            assembly = new Assembly();
            primaryStage.setScene(startGame());
            primaryStage.show();
        });
    }

    /**
     * Event handler for quitting game
     */
    @Override
    public void quit() {
        Platform.runLater(() -> {
            primaryStage.close();
        });
    }
}
