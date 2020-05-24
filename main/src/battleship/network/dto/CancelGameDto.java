package battleship.network.dto;

import java.io.Serializable;

public class CancelGameDto implements Serializable, ITypedDto {
    public static final String TYPE = "CancelGame";

    private String type = TYPE;

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
