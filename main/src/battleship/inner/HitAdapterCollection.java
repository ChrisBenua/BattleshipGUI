package battleship.inner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HitAdapterCollection implements IHitAdapterCollection {
    private List<IHitAdapter> hitAdapters = new ArrayList<>();

    @Override
    public void add(IHitAdapter adapter) {
        hitAdapters.add(adapter);
    }

    @Override
    public boolean isPointInsideAnyShip(Rectangle.Point point) {
        return findByPoint(point).isPresent();
    }

    @Override
    public boolean hit(Rectangle.Point target) {
        var adapter = findByPoint(target);
        return adapter.map(adapter_ -> adapter_.hit(target)).orElse(false);
    }

    @Override
    public ShipPartState shipPartState(Rectangle.Point point) {
        var adapter = findByPoint(point);
        return adapter.map(adapter_ -> adapter_.shipPartState(point))
                .orElseThrow(() -> new IllegalArgumentException("point is not inside any ship"));
    }

    @Override
    public int getSunkCount() {
        return hitAdapters.stream().map(el -> {
            var ship = el.getShip();
            var hit = ship.getHit();
            boolean isSunk = true;

            for (int i = 0; i < ship.getLength(); ++i) {
                isSunk &= hit[i];
            }

            return isSunk ? 1 : 0;
        }).reduce(0, Integer::sum);
    }

    private Optional<IHitAdapter> findByPoint(Rectangle.Point point) {
        return hitAdapters.stream().filter(adapter -> adapter.isPointInsideShip(point)).findFirst();
    }
}
