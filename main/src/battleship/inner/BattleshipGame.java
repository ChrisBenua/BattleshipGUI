package battleship.inner;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import battleship.network.IClientServer;
import battleship.network.IDtoEventsHandler;
import battleship.network.dto.GameStartingEventDto;
import battleship.network.dto.ShotInfoDto;
import battleship.network.dto.ShotResultDto;
import battleship.network.dto.ShotResultEnum;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Main game process class, contains all logic for interacting
 */
public class BattleshipGame implements IBattleshipGame {
    private static final int TOTAL_SHIPS = 10;
    public static final int OCEAN_SIZE = 10;
    private IHitAdapterFactory hitAdapterFactory;
    private IPlacementAdapter placementAdapter;
    private IShipFactory shipFactory;
    private IEventsLogger logger;
    private SimpleBooleanProperty hasOpponentProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isOpponentReady = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isPlayerReady = new SimpleBooleanProperty(false);
    private SimpleObjectProperty<GameTurn> gameTurn = new SimpleObjectProperty<>(GameTurn.CALCULATING);
    private String winner;
    private String loser;

    private int shotsFired = 0;
    private int hitCount = 0;
    private int shipsSunk = 0;
    private String opponentName;
    private String playerName;
    private SimpleBooleanProperty isMyTurn = new SimpleBooleanProperty();

    private IHitAdapterCollection hitAdapterCollection;
    private boolean[][] hitHistory;

    private CellState[][] opponentOceanState;

    private ISubscriber<RootPane.GameEvents> subscriber;
    private List<IRefreshable<RootPane.RefreshValues>> onUpdatePlayerFieldHandlers = new ArrayList<>();

    private IClientServer clientServer = null;

    @Override
    public ReadOnlyBooleanProperty getIsMyTurn() {
        return isMyTurn;
    }

    @Override
    public ReadOnlyObjectProperty<GameTurn> getGameTurn() {
        return gameTurn;
    }

    @Override
    public void setTurn(GameTurn turn) {
        gameTurn.set(turn);
    }

    /**
     * Construct new BattleshipGame instance with all dependencies
     * @param factory hits adapters factory
     * @param adapter placement handler
     * @param shipFactory ships factory
     * @param hitAdapterCollection hits handler
     * @param logger logs handler
     */
    public BattleshipGame(IHitAdapterFactory factory, IPlacementAdapter adapter, IShipFactory shipFactory,
                          IHitAdapterCollection hitAdapterCollection, IEventsLogger logger) {
        this.hitAdapterFactory = factory;
        this.placementAdapter = adapter;
        this.shipFactory = shipFactory;
        this.hitAdapterCollection = hitAdapterCollection;
        this.logger = logger;

        hitHistory = new boolean[OCEAN_SIZE][OCEAN_SIZE];
        this.opponentOceanState = new CellState[OCEAN_SIZE][OCEAN_SIZE];

        for (int i = 0; i < OCEAN_SIZE; ++i) {
            for (int j = 0; j < OCEAN_SIZE; ++j) {
                this.opponentOceanState[i][j] = CellState.EMPTY;
            }
        }

        placementAdapter.setHeight(OCEAN_SIZE);
        placementAdapter.setWidth(OCEAN_SIZE);
    }

    @Override
    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn.set(isMyTurn);
    }

    /**
     * Sets game events subscriber
     * @param subscriber subscriber
     */
    @Override
    public void setSubscriber(ISubscriber<RootPane.GameEvents> subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * places ships randomly
     */
    public void placeShipsRandomly() {
        Random random = new Random();
        List<Pair<Integer, Supplier<Ship>>> countToShipGen = new ArrayList<>() {
            {
                this.add(new Pair<Integer, Supplier<Ship>>(1, shipFactory::battleShip));
                this.add(new Pair<Integer, Supplier<Ship>>(2, shipFactory::cruiser));
                this.add(new Pair<Integer, Supplier<Ship>>(3, shipFactory::destroyer));
                this.add(new Pair<Integer, Supplier<Ship>>(4, shipFactory::submarine));
            }
        };
        for (final var entry : countToShipGen) {
            for (int shipNumber = 0; shipNumber < entry.getFirst(); ++shipNumber) {

                Ship ship = entry.getSecond().get();
                do {
                    int row = random.nextInt(10);
                    int col = random.nextInt(10);
                    boolean isHorizontal = random.nextBoolean();
                    ship.setBowRow(row);
                    ship.setBowColumn(col);
                    ship.setHorizontal(isHorizontal);
                } while (!placementAdapter.canAddShip(ship, true));
                hitAdapterCollection.add(hitAdapterFactory.hitAdapterFor(ship));
            }
        }
    }

    @Override
    public void addOnUpdateField(IRefreshable<RootPane.RefreshValues> onUpdateOpponentField) {
        this.onUpdatePlayerFieldHandlers.add(onUpdateOpponentField);
    }

    @Override
    public void processShotInfo(ShotInfoDto shotInfoDto) {
        int row = shotInfoDto.getRow(), column = shotInfoDto.getCol();

        shotsFired++;

        hitHistory[row][column] = true;
        var success = hitAdapterCollection.hit(Rectangle.Point.of(column, row));

        if (success == IHitAdapterCollection.HitResults.HIT) {

            if (clientServer != null) {
                this.clientServer.write(new ShotResultDto(row, column, ShotResultEnum.HIT));
            }
            this.isMyTurn.set(false);
            this.gameTurn.set(GameTurn.OPPONENT);
            logger.add(String.format("Игрок %s: (%d, %d) = повредил Ваш корабль", getOpponentName(), row, column));
        } else if (success == IHitAdapterCollection.HitResults.SUNK) {

            if (this.clientServer != null) {
                this.clientServer.write(new ShotResultDto(row, column, ShotResultEnum.SUNK));
            }

            if (isGameOver()) {
                this.gameTurn.set(GameTurn.END);
                if (Objects.nonNull(subscriber)) {
                    subscriber.accept(RootPane.GameEvents.END_OF_GAME);
                }
            } else {
                this.isMyTurn.set(false);
                this.gameTurn.set(GameTurn.OPPONENT);
            }
            this.logger.add(String.format("Игрок %s: (%d, %d) = потопил Ваш корабль %s", getOpponentName(), row, column, this.hitAdapterCollection.getLastSunkShipType()));
        } else {
            logger.add(String.format("Игрок %s: (%d, %d) = промазал", getOpponentName(), row, column));

            if (clientServer != null) {
                this.clientServer.write(new ShotResultDto(row, column, ShotResultEnum.MISS));
            }
            this.gameTurn.set(GameTurn.ME);
            this.isMyTurn.set(true);
        }

        this.onUpdatePlayerFieldHandlers.forEach(el -> el.refresh(RootPane.RefreshValues.ALL));

    }

    @Override
    public void shootAtOpponentField(int row, int column) {
        if (!opponentOceanState[row][column].equals(CellState.EMPTY)) {
            //logger.add("You have already shot at pos: (" + row + ", " + column + ")");
            if (Objects.nonNull(subscriber)) {
                subscriber.accept(RootPane.GameEvents.REPEATED_HIT);
            }
        } else {
            //hitHistory[row][column] = true;

            if (clientServer != null) {
                this.clientServer.write(new ShotInfoDto(row, column));
            }
            this.isMyTurn.set(false);
            this.gameTurn.set(GameTurn.CALCULATING);
        }
    }

    @Override
    public void setOpponentReadyStatus(boolean status) {
        this.isOpponentReady.set(status);
    }

    @Override
    public ObservableBooleanValue getOpponentReadyStatus() {
        return isOpponentReady;
    }

    @Override
    public void setPlayerReadyStatus(boolean status) {
        this.isPlayerReady.set(status);
    }

    @Override
    public ObservableBooleanValue getPlayerReadyStatus() {
        return this.isPlayerReady;
    }

    @Override
    public boolean isMyTurn() {
        return isMyTurn.get();
    }

    @Override
    public void processGameStartingEvent(GameStartingEventDto gameStartingEventDto) {
        setOpponentReadyStatus(true);
    }

    @Override
    public void processShotResult(ShotResultDto shotResultDto) {
        System.out.println("ProcessShotResult: " + shotResultDto);
        if (shotResultDto.getShotResult() == ShotResultEnum.MISS) {
            this.opponentOceanState[shotResultDto.getRow()][shotResultDto.getCol()] = CellState.MISS;
            this.logger.add(String.format("Игрoк %s (Вы):(%d, %d) = промазал", this.getPlayerName(), shotResultDto.getRow(), shotResultDto.getCol()));
            gameTurn.set(GameTurn.OPPONENT);
        } else if (shotResultDto.getShotResult() == ShotResultEnum.HIT) {
            this.opponentOceanState[shotResultDto.getRow()][shotResultDto.getCol()] = CellState.DAMAGED;
            this.isMyTurn.set(true);
            this.gameTurn.set(GameTurn.ME);
            this.logger.add(String.format("Игрок %s (Вы):(%d, %d) = повредили корабль оппонента", this.getPlayerName(), shotResultDto.getRow(), shotResultDto.getCol()));
        } else {
            this.opponentOceanState[shotResultDto.getRow()][shotResultDto.getCol()] = CellState.DAMAGED;

            int res = markShipAsSunk(shotResultDto.getRow(), shotResultDto.getCol(), -1, -1);
            shipsSunk++;

            if (this.isGameOver()) {
                gameTurn.set(GameTurn.END);
                if (Objects.nonNull(subscriber)) {
                    subscriber.accept(RootPane.GameEvents.END_OF_GAME);
                }
            } else {
                gameTurn.set(GameTurn.ME);
                this.isMyTurn.set(true);
            }

            this.logger.add(String.format("Игрок %s (Вы):(%d, %d) = потопили корабль противника %s", this.getPlayerName(), shotResultDto.getRow(), shotResultDto.getCol(), shipFactory.shipByLength(res).getShipType()));
        }

        this.onUpdatePlayerFieldHandlers.forEach(el -> el.refresh(RootPane.RefreshValues.ALL));
    }

    private int markShipAsSunk(int row, int col, int prevRow, int prevCol) {
        int[] drow = {-1, 0, 1, 0};
        int[] dcol = {0, -1, 0, 1};
        int res = 1;
        this.opponentOceanState[row][col] = CellState.SUNK;

        for (int dir = 0; dir < drow.length; ++dir) {
            int newRow = row + drow[dir];
            int newCol = col + dcol[dir];

            if (newRow >= 0 && newCol >= 0 && newRow < OCEAN_SIZE && newCol < OCEAN_SIZE
                    && this.opponentOceanState[newRow][newCol] == CellState.DAMAGED
                    && (newRow != prevRow || newCol != prevCol)) {
                res += markShipAsSunk(newRow, newCol, row, col);
            }
        }

        return res;
    }

    /**
     * Gets amount of shots fired in game
     * @return amount of shots fired in game
     */
    @Override
    public int getShotsFired() {
        return shotsFired;
    }

    /**
     * Gets hits amount
     * @return hits amount
     */
    @Override
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Gets amount of sunk ships
     * @return amount of sunk ships
     */
    @Override
    public int getShipsSunk() {
        return hitAdapterCollection.getSunkCount();
    }

    /**
     * Gets amount of damaged ships
     * @return amount of damaged ships
     */
    @Override
    public int getDamagedShipsCount() {
        return hitAdapterCollection.getDamagedCount();
    }

    /**
     * Gets amount of untouched ships
     * @return amount of untouched ships
     */
    @Override
    public int getUntouchedShipsCount() {
        return TOTAL_SHIPS - getShipsSunk() - getDamagedShipsCount();
    }

    /**
     * Gets game status
     * @return true if all ships are sunk false otherwise
     */
    @Override
    public boolean isGameOver() {
        boolean opponentWon = hitAdapterCollection.getSunkCount() == TOTAL_SHIPS;
        boolean playerWon = shipsSunk == TOTAL_SHIPS;
        if (opponentWon) {
            this.winner = opponentName;
            this.loser = playerName;
        } else {
            this.winner = playerName;
            this.loser = opponentName;
        }
        return opponentWon || playerWon;
    }

    /**
     * Gets Cell in Ocean States
     * @return cells in ocean states
     */
    @Override
    public CellState[][] getPlayerOceanState() {
        CellState[][] states = new CellState[OCEAN_SIZE][OCEAN_SIZE];

        for (int row = 0; row < OCEAN_SIZE; ++row) {
            for (int col = 0; col < OCEAN_SIZE; ++col) {
                if (hitAdapterCollection.isPointInsideAnyShip(Rectangle.Point.of(col, row))) {
                    var state = hitAdapterCollection.shipPartState(Rectangle.Point.of(col, row));

                    if (state == ShipPartState.ALIVE) {
                        states[row][col] = CellState.EMPTY;
                    } else if (state == ShipPartState.DAMAGED) {
                        states[row][col] = CellState.DAMAGED;
                    } else if (state == ShipPartState.SUNK) {
                        states[row][col] = CellState.SUNK;
                    }

                } else {
                    if (hitHistory[row][col]) {
                        states[row][col] = CellState.MISS;
                    } else {
                        states[row][col] = CellState.EMPTY;
                    }
                }
            }
        }

        return states;
    }

    @Override
    public CellState[][] getOpponentOceanState() {
        return this.opponentOceanState;
    }

    @Override
    public void setClientServer(IClientServer clientServer) {
        this.clientServer = clientServer;


        hasOpponentProperty.set(Objects.nonNull(this.clientServer));
    }

    @Override
    public IClientServer getClientServer() {
        return clientServer;
    }

    @Override
    public SimpleBooleanProperty hasOpponent() {
        return this.hasOpponentProperty;
    }

    @Override
    public void setOpponentName(String name) {
        this.opponentName = name;
    }

    @Override
    public String getOpponentName() {
        return this.opponentName;
    }

    @Override
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public String getWinnerName() {
        return winner;
    }

    @Override
    public String getLoserName() {
        return loser;
    }

    @Override
    public int getEnemyShotsCount() {
        return this.shotsFired;
    }

    @Override
    public int getPlayerShotsCount() {
        return Arrays.stream(this.opponentOceanState).flatMap(el -> Arrays.stream(el)).map(el -> {
            if (el != CellState.EMPTY) {
                return 1;
            }
            return 0;
        }).reduce(0, Integer::sum);
    }

    public static class Pair<T, U> {
        private T first;
        private U second;

        /**
         * Create a new Pair with 'first' field of type T
         * And 'second' field of type U
         * @param first value of first element of pair
         * @param second value of second element of pair
         */
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        /**
         * Gets this Pair first element
         * @return first element in pair
         */
        public T getFirst() {
            return first;
        }

        /**
         * Gets this Pair second element
         * @return second element in pair
         */
        public U getSecond() {
            return second;
        }

        /**
         * This static factory function create a new Pair of given first and second element
         * @param first first Pair element
         * @param second second Pair element
         * @param <T> type of first Pair element
         * @param <U> type of second Pair element
         * @return new Pair with giver first and second element
         */
        public static <T, U> Pair<T, U> of(T first, U second) {
            return new Pair<>(first, second);
        }
    }

    /**
     * Shots results enum
     */
    public static enum ShotResults {
        MISS, HIT, REPEAT_SHOT
    }

    public static void main(String[] args) {
        var game = new BattleshipGame(new HitAdapterFactory(), new PlacementAdapter(), new ShipFactory(),
                new HitAdapterCollection(), new EventsLogger());
        game.placeShipsRandomly();
        System.out.println(game.hitCount);
    }
}
