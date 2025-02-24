package shoon.board.hotarticle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HotArticleListRepositoryTest {

    @Autowired
    HotArticleListRepository hotArticleListRepository;

    @Test
    void addTest() throws InterruptedException {

        LocalDateTime time = LocalDateTime.of(2025, 2, 18, 0, 0);
        long limit = 3;

        // when
        hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(100));
        hotArticleListRepository.add(2L, time, 3L, limit, Duration.ofSeconds(100));
        hotArticleListRepository.add(3L, time, 1L, limit, Duration.ofSeconds(100));
        hotArticleListRepository.add(4L, time, 5L, limit, Duration.ofSeconds(100));
        hotArticleListRepository.add(5L, time, 4L, limit, Duration.ofSeconds(100));

        // then
        List<Long> articleIds =  hotArticleListRepository.readAll("20250218");

        Assertions.assertThat(articleIds).hasSize(Long.valueOf(limit).intValue());
        Assertions.assertThat(articleIds.get(0)).isEqualTo(4);
        Assertions.assertThat(articleIds.get(1)).isEqualTo(5);
        Assertions.assertThat(articleIds.get(2)).isEqualTo(2);

//        TimeUnit.SECONDS.sleep(5);
//        Assertions.assertThat(hotArticleListRepository.readAll("20240723")).isEmpty();

    }
}