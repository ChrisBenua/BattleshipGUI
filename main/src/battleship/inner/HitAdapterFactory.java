package battleship.inner;

public class HitAdapterFactory implements IHitAdapterFactory {

    @Override
    public IHitAdapter hitAdapterFor(Ship ship) {
        return new HitAdapter(ship);
    }
}
