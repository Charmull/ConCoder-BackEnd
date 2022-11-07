package oncoding.concoder.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Level;
import oncoding.concoder.repository.LevelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    public List<Level> getLevels() {
        return levelRepository.findAll();
    }
}
