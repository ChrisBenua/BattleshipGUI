package battleship.inner;

public interface IShipFactory {
    public Ship submarine();

    public Ship destroyer();

    public Ship cruiser();

    public Ship battleShip();
}
