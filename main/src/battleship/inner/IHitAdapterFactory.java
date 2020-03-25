package battleship.inner;

public interface IHitAdapterFactory {
    IHitAdapter hitAdapterFor(Ship ship);
}
