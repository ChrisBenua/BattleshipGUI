package battleship.network;

public class Assembly {
    private IDtoReader dtoReader = new DtoReader();
    private BattleshipServer battleshipServer = null;
    private BattleshipClient battleshipClient = null;

    public IDtoReader getDtoReader() {
        return dtoReader;
    }

    public BattleshipServer getBattleshipServer() {
        if (battleshipServer == null) {
            battleshipServer = new BattleshipServer(this);
        }
        return battleshipServer;
    }

    public BattleshipClient getBattleshipClient() {
        if (battleshipClient == null) {
            battleshipClient = new BattleshipClient(this);
        }
        return battleshipClient;
    }
}
