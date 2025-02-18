package shoon.board.view.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoon.board.view.entity.ArticleViewCount;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {


    @Autowired
    ArticleViewCountBackUpRepository articleViewCountBackUpRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    void updateViewCountTest() {
        ArticleViewCount articleViewCount = articleViewCountBackUpRepository.save(
                ArticleViewCount.init(1L, 0L)
        );

        em.flush();
        em.clear();

        int result1 = articleViewCountBackUpRepository.updateViewCount(1L, 100L);
        int result2 = articleViewCountBackUpRepository.updateViewCount(1L, 300L);
        int result3 = articleViewCountBackUpRepository.updateViewCount(1L, 200L);

        Assertions.assertThat(result1).isEqualTo(1);
        Assertions.assertThat(result2).isEqualTo(1);
        Assertions.assertThat(result3).isEqualTo(0);
    }
}