package oncoding.concoder.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CategoryDto {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String name;
    }
}
