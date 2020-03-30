package battleship.inner;

/**
 * Factory for creating Ship's hit adapters
 */
public class HitAdapterFactory implements IHitAdapterFactory {

    /**
     * Generates given ship's adapter
     * @param ship ship to create hit adapter for
     * @return IHitAdapter realization
     */
    @Override
    public IHitAdapter hitAdapterFor(Ship ship) {
        return new HitAdapter(ship);
    }
}
