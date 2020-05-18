package battleship.network;

public class Assembly {
    private IDtoReader dtoReader = new DtoReader();

    public IDtoReader getDtoReader() {
        return dtoReader;
    }
}
