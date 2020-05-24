package battleship.gui;

import battleship.guiservices.Assembly;
import battleship.guiservices.IOceanCellEventHandlerFactory;
import battleship.inner.IBattleshipGame;
import battleship.inner.Ship;
import battleship.network.dto.CancelGameDto;
import battleship.network.dto.GameStartingEventDto;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class of JavaFX application
 */
public class BattleshipApplication extends Application implements IEndGameReactor, IBeforeGreetingHandler, IShipsContainerButtonsClickHandler {

    private Assembly assembly = new Assembly();
    private battleship.network.Assembly networkAssembly = new battleship.network.Assembly();
    private Stage primaryStage;
    private boolean isCoordsEnterStageShown = false;
    private IOnFindOpponentWindowCloseHandler onFindOpponentWindowCloseHandler;
    private ShipsPlacingPane pane;
    private BooleanBinding binding;

    /**
     * Generates Scene and starts game initializing it with Assembly instance
     * @return new Scene
     */
    private Scene startGame() {
        primaryStage.setTitle("Battleship Game");

        var battleshipGame = assembly.getGame();
        var logger = assembly.getEventsLogger();

        var eventsLogContainer = new EventLogsContainer();
        //var statsContainer = new GameStatsContainer();
        logger.subscribe(eventsLogContainer);

        //battleshipGame.placeShipsRandomly();
        RootPane root = new RootPane(battleshipGame);

        this.assembly.getGame().setMyTurn(!isServer());
        this.assembly.getGame().setTurn(isServer() ? IBattleshipGame.GameTurn.OPPONENT : IBattleshipGame.GameTurn.ME);
        this.networkAssembly.getDtoReader().addEventsHandler(root);


        root.setApplication(this);
        battleshipGame.setSubscriber(root);
        var oceanGridPane = new OceanGridPane(battleshipGame.getPlayerOceanState(),
                assembly.getOceanCellStateColorMapper(), assembly.getOceanCellEventHandlerFactory(), root, battleshipGame.getIsMyTurn());
        var myOceanGridPane = new OceanGridPane(battleshipGame.getOpponentOceanState(), assembly.getOceanCellStateColorMapper(),
                new IOceanCellEventHandlerFactory() {
                    @Override
                    public EventHandler<MouseEvent> getOceanCellEventHandler(int row, int col, IRefreshable<RootPane.RefreshValues> parent) {
                        return null;
                    }
                }, root, new SimpleBooleanProperty(false));
        root.setChildren(oceanGridPane, eventsLogContainer, myOceanGridPane);

        assembly.getGame().addOnUpdateField(root);

        var scene = new Scene(root, 500, 500);
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(520);
        scene.getStylesheets().add(getClass().getResource("text-area.css").toExternalForm());

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!isCoordsEnterStageShown && this.assembly.getGame().getGameTurn().get() == IBattleshipGame.GameTurn.ME) {
                    isCoordsEnterStageShown = true;
                    System.out.println("DETECTED");
                    Scene coordsScene = new Scene(new EnterCoordinatesPane((row, col) -> {
                        this.assembly.getGame().shootAtOpponentField(row, col);
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
        primaryStage.setTitle("Battleship game");
        this.networkAssembly.getDtoReader().addEventsHandler(this.assembly.getGame());

        this.assembly.getGame().getOpponentReadyStatus().addListener((__, ___, new_) -> {
            System.out.println("getOpponentReadyStatus: " + new_);
        });

        this.assembly.getGame().getPlayerReadyStatus().addListener((__, ___, new_) -> {
            System.out.println("getPlayerReadyStatus: " + new_);
        });

        binding = Bindings.and(
                this.assembly.getGame().getOpponentReadyStatus(),
                this.assembly.getGame().getPlayerReadyStatus()
        );

        binding.addListener((__, ___, new_) -> {
            System.out.println("Binding: " + new_);
            if (new_) {
                var capturedThis = this;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            capturedThis.primaryStage.setScene(capturedThis.startGame());
                        });
                    }
                }, 3000);
            }
        });

        pane = new ShipsPlacingPane(this.assembly.getGame(), assembly.getShipFactory(), assembly.getGame().hasOpponent(), this);
        this.onFindOpponentWindowCloseHandler = pane;
        var scene = new Scene(pane, 500, 500);


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isServer() {
        var params = getParameters().getRaw();
        String mode = params.size() > 0 ? params.get(0).toLowerCase() : "server";

        return mode.equals("server");
    }

    @Override
    public void onFindOpponentButtonClicked() {
        if (isServer()) {
            startServer(null).show();
        } else {
            startClient(null).show();
        }
    }

    @Override
    public void onPlayButtonClicked(List<Ship> ships) {
        this.assembly.getHitAdapterCollection().clear();
        for (var ship : ships) {
            this.assembly.getHitAdapterCollection().add(this.assembly.getHitAdapterFactory().hitAdapterFor(ship));
        }

        this.assembly.getGame().getClientServer().write(new GameStartingEventDto());
        this.assembly.getGame().setPlayerReadyStatus(true);
    }

    public Stage startServer(Stage stage_) {
        Stage stage = stage_;
        if (Objects.isNull(stage)) {
            stage = new Stage();
        }
        stage.setTitle("Battleship Game Server");

        var serverStartPane = new ServerStartPane(assembly.getGame(), this, networkAssembly);
        var scene = new Scene(serverStartPane, 500, 200);
        stage.setMinHeight(100);
        stage.setMinWidth(200);
        stage.setScene(scene);

        stage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - 250);
        stage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - 100);

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (event) -> {
            this.onFindOpponentWindowCloseHandler.onOpponentWindowClosed();
        });

        return stage;
    }

    public Stage startClient(Stage stage_) {
        Stage stage = stage_;
        if (Objects.isNull(stage)) {
            stage = new Stage();
        }
        stage.setTitle("Battleship Game Client");

        var serverStartPane = new ClientStartPane(assembly.getGame(), this, networkAssembly);
        var scene = new Scene(serverStartPane, 500, 300);
        stage.setMinHeight(150);
        stage.setMinWidth(200);
        stage.setScene(scene);

        stage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - 250);
        stage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - 150);

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (event) -> {
            this.onFindOpponentWindowCloseHandler.onOpponentWindowClosed();
        });

        return stage;
    }

    @Override
    public void cancel() {
        this.assembly.getGame().getClientServer().write(new CancelGameDto(this.assembly.getGame().getPlayerName()));
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

    @Override
    public void showGreetingPaneIn(Stage stage) {
        var greetPane = new GreetingsPane(assembly.getGame(), assembly.getGame().getClientServer(), networkAssembly);
        var scene = new Scene(greetPane, 500, 200);
        stage.setScene(scene);
    }


}
