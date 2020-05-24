package battleship.inner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles hits for collection of ships
 */
public class HitAdapterCollection implements IHitAdapterCollection {
    private List<IHitAdapter> hitAdapters = new ArrayList<>();
    private Optional<IEventsLogger> logger = Optional.empty();
    private String lastSunkShipType = null;

    /**
     * Adds hit adapter of ship
     * @param adapter hit adapter of ship
     */
    @Override
    public void add(IHitAdapter adapter) {
        hitAdapters.add(adapter);
    }

    /**
     * Checks if given point is inside any ship
     * @param point target point
     * @return true if given point is inside any ship false otherwise
     */
    @Override
    public boolean isPointInsideAnyShip(Rectangle.Point point) {
        return findByPoint(point).isPresent();
    }

    /**
     * Perform a shot to given point
     * @param target point to perform shot in
     * @return MISS if target point is outside, HIT if ship was damaged, SUNK if ship is sunk after this shot
     */
    @Override
    public HitResults hit(Rectangle.Point target) {
        var adapter = findByPoint(target);
        return adapter.map(adapter_ -> {
            boolean result = adapter_.hit(target);
             if (result) {
                 if (adapter_.getShip().isSunk()) {
                     lastSunkShipType = adapter_.getShip().getShipType();
                     return HitResults.SUNK;
                 } else {
                     return HitResults.HIT;
                 }
            } else {
                return HitResults.MISS;
            }
        }).orElse(HitResults.MISS);
    }

    /**
     * Gets state of ship part
     * @param point
     * @return
     */
    @Override
    public ShipPartState shipPartState(Rectangle.Point point) {
        var adapter = findByPoint(point);
        return adapter.map(adapter_ -> adapter_.shipPartState(point))
                .orElseThrow(() -> new IllegalArgumentException("point is not inside any ship"));
    }

    /**
     * Gets amount of sunk ships
     * @return amount of sunk ships
     */
    @Override
    public int getSunkCount() {
        return hitAdapters.stream().map(el -> {
            var ship = el.getShip();
            boolean isSunk = ship.isSunk();

            return isSunk ? 1 : 0;
        }).reduce(0, Integer::sum);
    }

    /**
     * Gets amount of damaged ships
     * @return amount of damaged ships
     */
    @Override
    public int getDamagedCount() {
        return hitAdapters.stream().map(el -> {
            var hit = el.getShip().getHit();

            boolean any = false;
            boolean all = true;
            for (boolean ht : hit) {
                any |= ht;
                all &= ht;
            }

            return (any && !all) ? 1 : 0;
        }).reduce(0, Integer::sum);
    }

    /**
     * Gets type of last sunk ship
     * @return type of last sunk ship
     */
    @Override
    public String getLastSunkShipType() {
        return lastSunkShipType;
    }

    /**
     * Clears ships in hitAdapter
     */
    @Override
    public void clear() {
        hitAdapters.clear();
    }

    /**
     * Finds Ship's hit adapter by given point
     * @param point target point
     * @return IHitAdapter if there is satisfying, empty otherwise
     */
    private Optional<IHitAdapter> findByPoint(Rectangle.Point point) {
        return hitAdapters.stream().filter(adapter -> adapter.isPointInsideShip(point)).findFirst();
    }
}
