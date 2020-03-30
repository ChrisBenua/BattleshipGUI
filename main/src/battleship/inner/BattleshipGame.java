package battleship.inner;

import battleship.gui.RootPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

    private int shotsFired = 0;
    private int hitCount = 0;

    private IHitAdapterCollection hitAdapterCollection;
    private boolean[][] hitHistory;

    private ISubscriber<RootPane.GameEvents> subscriber;

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

        placementAdapter.setHeight(OCEAN_SIZE);
        placementAdapter.setWidth(OCEAN_SIZE);
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

    /**
     * Performs shot at given position
     * @param row row of shot
     * @param column column of shot
     * @return ShotResult
     */
    @Override
    public ShotResults shootAt(int row, int column) {
        if (hitHistory[row][column]) {
            logger.add("You have already shot at pos: (" + row + ", " + column + ")");
            if (Objects.nonNull(subscriber)) {
                subscriber.accept(RootPane.GameEvents.REPEATED_HIT);
            }
            return ShotResults.REPEAT_SHOT;
        }
        shotsFired++;
        hitHistory[row][column] = true;
        var success = hitAdapterCollection.hit(Rectangle.Point.of(column, row));

        if (success == IHitAdapterCollection.HitResults.HIT) {
            this.hitCount++;
            logger.add("Success! You hit the target at pos (" + row + ", " + column + ")");
            return ShotResults.HIT;
        } else if (success == IHitAdapterCollection.HitResults.SUNK) {
            this.hitCount++;
            if (hitAdapterCollection.getSunkCount() == TOTAL_SHIPS) {
                if (Objects.nonNull(subscriber)) {
                    subscriber.accept(RootPane.GameEvents.END_OF_GAME);
                }
            }
            return ShotResults.HIT;
        } else {
            logger.add("Unfortunately, you missed! :-(");
            return ShotResults.MISS;
        }
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
        return hitAdapterCollection.getSunkCount() == TOTAL_SHIPS;
    }

    /**
     * Gets Cell in Ocean States
     * @return cells in ocean states
     */
    @Override
    public CellState[][] getOceanState() {
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
