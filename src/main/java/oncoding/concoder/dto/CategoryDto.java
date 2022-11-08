package oncoding.concoder.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

public class CategoryDto {
    @Getter
    @ToString
    public static class Response {
        private UUID id;
        private String name;
    }
}
