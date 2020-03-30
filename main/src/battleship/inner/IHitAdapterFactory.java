package battleship.inner;

/**
 * Factory for creating hit adapter/wrapper for given ship
 */
public interface IHitAdapterFactory {
    /**
     * Generates adapter for given ship
     * @param ship given ship
     * @return IHitAdapter realization
     */
    IHitAdapter hitAdapterFor(Ship ship);
}
