package battleship.network.dto;

import java.io.Serializable;

public class GreetingsDto implements Serializable, ITypedDto {
    public static final String TYPE = "Greeting";

    private String name;

    public final String type;

    public GreetingsDto(String name) {
        this.name = name;
        this.type = TYPE;
    }

    public GreetingsDto() {
        this.type = TYPE;
    }

    public GreetingsDto(String name, String type) {
        this(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
