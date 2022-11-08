package oncoding.concoder.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class LevelDto {
    @Getter
    @Setter
    @ToString
    public static class Response {
        private UUID id;
        private String name;
    }
}
