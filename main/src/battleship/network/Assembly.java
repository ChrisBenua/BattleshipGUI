package battleship.network;

/**
 * Assembly for network-like classes
 */
public class Assembly {
    private IDtoReader dtoReader = new DtoReader();
    private BattleshipServer battleshipServer = null;
    private BattleshipClient battleshipClient = null;

    public Assembly() {
        battleshipServer = new BattleshipServer(this);
        battleshipClient = new BattleshipClient(this);
    }

    /**
     * Gets dto-reader
     * @return dto-reader
     */
    public IDtoReader getDtoReader() {
        return dtoReader;
    }

    /**
     * Gets BattleshipServer
     * @return BattleshipServer
     */
    public BattleshipServer getBattleshipServer() {
        return battleshipServer;
    }

    /**
     * Gets BattleshipClient
     * @return BattleshipClient
     */
    public BattleshipClient getBattleshipClient() {
        return battleshipClient;
    }
}
