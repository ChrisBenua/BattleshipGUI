package battleship.network.dto;

import java.io.Serializable;

/**
 * DTO for sending game starting event
 */
public class GameStartingEventDto implements Serializable, ITypedDto {
    public static final String TYPE = "GameStarting";

    private String type = TYPE;

    @Override
    public String getType() {
        return TYPE;
    }
}
