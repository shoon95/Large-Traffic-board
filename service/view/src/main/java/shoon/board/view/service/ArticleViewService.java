package shoon.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shoon.board.view.repository.ArticleViewCountRepository;
import shoon.board.view.repository.ArticleViewDistributedLockRepository;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;
    private final ArticleViewCountRepository articleViewCountRepository;
    private static final int BACK_UP_BACH_SIZE = 100;
    private static final Duration TTL = Duration.ofMinutes(10);

    public Long increase(Long articleId, Long userId) {

        if (!articleViewDistributedLockRepository.lock(articleId, userId, TTL)) {
            return articleViewCountRepository.read(articleId);
        }

        Long count = articleViewCountRepository.increase(articleId);

        if (count % BACK_UP_BACH_SIZE == 0) {
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }

        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
