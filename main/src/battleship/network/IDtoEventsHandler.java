package battleship.network;

import battleship.network.dto.*;

/**
 * Interface for handling events about reading following instances from socket
 */
public interface IDtoEventsHandler {
    /**
     * Processes greetings
     * @param greetingsDto DTO that was read from socket connection
     */
    public default void processGreetings(GreetingsDto greetingsDto) {}

    /**
     * Processes shot info
     * @param shotInfoDto  DTO that was read from socket connection
     */
    public default void processShotInfo(ShotInfoDto shotInfoDto) {}

    /**
     * Processes shot result
     * @param shotResultDto  DTO that was read from socket connection
     */
    public default void processShotResult(ShotResultDto shotResultDto) {}

    /**
     * Processes game starting event
     * @param gameStartingEventDto  DTO that was read from socket connection
     */
    public default void processGameStartingEvent(GameStartingEventDto gameStartingEventDto) {}

    /**
     * Processes Game cancellation game event
     * @param cancelGameDto  DTO that was read from socket connection
     */
    public default void processCancelGameEvent(CancelGameDto cancelGameDto) {}
}
