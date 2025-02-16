package shoon.board.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import shoon.board.common.config.P6SpyConfig;

@SpringBootApplication
@Import(P6SpyConfig.class)
public class LikeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeApplication.class);
    }
}
