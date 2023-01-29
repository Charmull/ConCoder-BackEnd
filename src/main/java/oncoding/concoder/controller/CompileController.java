package oncoding.concoder.controller;

import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.CompileDTO;
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
    public String test(@RequestBody String code) {
        try {
            return compileService.run(code,"").get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
    }

}
