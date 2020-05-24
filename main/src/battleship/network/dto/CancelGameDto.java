package battleship.network.dto;

import java.io.Serializable;

/**
 * Dto for sending Cancel game event dto
 */
public class CancelGameDto implements Serializable, ITypedDto {
    public static final String TYPE = "CancelGame";

    /**
     * Type of dto
     */
    private String type = TYPE;

    /**
     * Name of player, who initiated cancellation
     */
    private String initiator;

    public CancelGameDto() {}

    public CancelGameDto(String initiator) {
        this.initiator = initiator;
    }

    public String getInitiator() {
        return initiator;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
