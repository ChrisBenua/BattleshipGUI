package battleship.inner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BattleshipGame implements IBattleshipGame {
    private static final int TOTAL_SHIPS = 10;
    public static final int OCEAN_SIZE = 10;
    private IHitAdapterFactory hitAdapterFactory;
    private IPlacementAdapter placementAdapter;
    private IShipFactory shipFactory;

    private int shotsFired = 0;
    private int hitCount = 0;

    private IHitAdapterCollection hitAdapterCollection;
    private boolean[][] hitHistory;

    public BattleshipGame(IHitAdapterFactory factory, IPlacementAdapter adapter, IShipFactory shipFactory,
                          IHitAdapterCollection hitAdapterCollection) {
        this.hitAdapterFactory = factory;
        this.placementAdapter = adapter;
        this.shipFactory = shipFactory;
        this.hitAdapterCollection = hitAdapterCollection;

        placementAdapter.setHeight(OCEAN_SIZE);
        placementAdapter.setWidth(OCEAN_SIZE);
    }

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
    public boolean shootAt(int row, int column) {
        shotsFired++;
        hitHistory[row][column] = true;
        boolean success = hitAdapterCollection.hit(Rectangle.Point.of(column, row));
        this.hitCount += success ? 1 : 0;
        return success;
    }

    @Override
    public int getShotsFired() {
        return shotsFired;
    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getShipsSunk() {
        return hitAdapterCollection.getSunkCount();
    }

    @Override
    public boolean isGameOver() {
        return hitAdapterCollection.getSunkCount() == TOTAL_SHIPS;
    }

    @Override
    public OceanState[][] getOceanState() {
        OceanState[][] states = new OceanState[OCEAN_SIZE][OCEAN_SIZE];

        for (int row = 0; row < OCEAN_SIZE; ++row) {
            for (int col = 0; col < OCEAN_SIZE; ++col) {
                if (hitAdapterCollection.isPointInsideAnyShip(Rectangle.Point.of(col, row))) {
                    var state = hitAdapterCollection.shipPartState(Rectangle.Point.of(col, row));

                    if (state == ShipPartState.ALIVE) {
                        states[row][col] = OceanState.EMPTY;
                    } else if (state == ShipPartState.DAMAGED) {
                        states[row][col] = OceanState.DAMAGED;
                    } else if (state == ShipPartState.SUNK) {
                        states[row][col] = OceanState.SUNK;
                    }

                } else {
                    if (hitHistory[row][col]) {
                        states[row][col] = OceanState.MISS;
                    } else {
                        states[row][col] = OceanState.EMPTY;
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

    public static void main(String[] args) {
        var game = new BattleshipGame(new HitAdapterFactory(), new PlacementAdapter(), new ShipFactory(),
                new HitAdapterCollection());
        game.placeShipsRandomly();
        System.out.println(game.hitCount);
    }
}
