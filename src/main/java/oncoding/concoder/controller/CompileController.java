package oncoding.concoder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.CompileDto;
import oncoding.concoder.dto.CompileDto.Response;
import oncoding.concoder.service.CompileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile")
@RequiredArgsConstructor
public class CompileController {
    private final CompileService compileService;

    @PostMapping("")
    public List<CompileDto.Response> test(@RequestBody CompileDto.CreateRequest req) {
            List<CompileDto.Response> responses = new ArrayList<>();
            for (int i = 0; i<req.getInputs().size(); i++) {
                try {
                    responses.add(compileService.run(req.getCode(), req.getInputs().get(i)).get());
                }
                catch (ExecutionException | InterruptedException e) {
                    responses.add(new CompileDto.Response("[Error] timeout", -1L));
                }
                catch (IOException e) {
                    responses.add(new CompileDto.Response("[Error] "+e.getMessage(), -1L));
                }
            }
            return responses;

    }

}
