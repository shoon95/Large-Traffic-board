package shoon.board.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import shoon.board.common.config.P6SpyConfig;

@SpringBootApplication
@Import(P6SpyConfig.class)
@EntityScan(basePackages = "shoon.board")
@EnableJpaRepositories(basePackages= "shoon.board")
public class LikeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeApplication.class);
    }
}
