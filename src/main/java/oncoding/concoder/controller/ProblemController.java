package oncoding.concoder.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.CategoryDto;
import oncoding.concoder.dto.LevelDto;
import oncoding.concoder.dto.ProblemDto;
import oncoding.concoder.mapper.CategoryDtoMapper;
import oncoding.concoder.mapper.LevelDtoMapper;
import oncoding.concoder.mapper.ProblemDtoMapper;
import oncoding.concoder.service.CategoryService;
import oncoding.concoder.service.LevelService;
import oncoding.concoder.service.ProblemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;
    private final CategoryService categoryService;
    private final LevelService levelService;

    private final CategoryDtoMapper categoryDtoMapper;
    private final LevelDtoMapper levelDtoMapper;
    private final ProblemDtoMapper problemDtoMapper;

    @GetMapping("/categories")
    public List<CategoryDto.Response> getCategories() {
        return categoryDtoMapper.toResponseList(categoryService.getCategories());
    }

    @GetMapping("/levels")
    public List<LevelDto.Response> getLevels() {
        return levelDtoMapper.toResponseList(levelService.getLevels());
    }

    @GetMapping("/random")
    public List<ProblemDto.AllResponse> getProblems(@RequestParam("standard") String standard, @RequestParam("id") UUID id) {
        return problemDtoMapper.toAllResponseList(problemService.getProblemsByStandard(standard, id));
    }

    @GetMapping("")
    public ProblemDto.AllResponse getProblem(@RequestParam("number") int number) {
        return problemDtoMapper.toAllResponse(problemService.getProblemByNumber(number));
    }
}
