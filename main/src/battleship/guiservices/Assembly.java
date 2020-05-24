package battleship.guiservices;

import battleship.inner.*;

/**
 * Class for storing all interfaces realizations
 */
public class Assembly {
    private IHitAdapterFactory hitAdapterFactory = new HitAdapterFactory();

    private IPlacementAdapter placementAdapter = new PlacementAdapter();

    private IShipFactory shipFactory = new ShipFactory();

    private IHitAdapterCollection hitAdapterCollection = new HitAdapterCollection();

    private IEventsLogger eventsLogger = new EventsLogger();

    private IBattleshipGame game = new BattleshipGame(hitAdapterFactory, placementAdapter, shipFactory, hitAdapterCollection, eventsLogger);

    private IOceanCellEventHandlerFactory oceanCellEventHandlerFactory = new OceanCellEventHandlerFactory(game);

    private IOceanCellStateColorMapper oceanCellStateColorMapper = new OceanCellStateColorMapper();

    public Assembly() {
    }

    /**
     * Gets hit adapter factory
     * @return IHitAdapterFactory realization
     */
    public IHitAdapterFactory getHitAdapterFactory() {
        return hitAdapterFactory;
    }

    /**
     * Gets placement adapter
     * @return IPlacementAdapter realization
     */
    public IPlacementAdapter getPlacementAdapter() {
        return placementAdapter;
    }

    /**
     * Gets ship factory
     * @return IShipFactory realization
     */
    public IShipFactory getShipFactory() {
        return shipFactory;
    }

    /**
     * Gets hit adapter collection
     * @return IHitAdapterCollection realization
     */
    public IHitAdapterCollection getHitAdapterCollection() {
        return hitAdapterCollection;
    }

    /**
     * Gets current battleship game
     * @return current IBattleshipGame realization
     */
    public IBattleshipGame getGame() {
        return game;
    }

    /**
     * Gets ocean cell event handler factory
     * @return IOceanCellEventHandlerFactory realization
     */
    public IOceanCellEventHandlerFactory getOceanCellEventHandlerFactory() {
        return oceanCellEventHandlerFactory;
    }

    /**
     * Gets ocean cell state color mapper
     * @return IOceanCellStateColorMapper realization
     */
    public IOceanCellStateColorMapper getOceanCellStateColorMapper() {
        return oceanCellStateColorMapper;
    }

    /**
     * Gets events logger
     * @return IEventsLogger realization
     */
    public IEventsLogger getEventsLogger() {
        return eventsLogger;
    }
}
