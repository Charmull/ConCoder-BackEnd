package oncoding.concoder.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CompileDto {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class CreateRequest {
        private String code;
        private List<String> inputs;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Response {
        private String testCaseId;
        private String output;
        private Long time;
    }

}
