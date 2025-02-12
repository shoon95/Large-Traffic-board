package shoon.board.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import shoon.board.common.config.P6SpyConfig;

@SpringBootApplication
@Import(P6SpyConfig.class)
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class);
    }
}
