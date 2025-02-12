package shoon.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import shoon.board.comment.repository.CommentRepositoryV2;
import shoon.board.comment.service.request.CommentCreateRequestV2;
import shoon.board.comment.service.request.CommentPageResponse;
import shoon.board.comment.service.response.CommentResponse;

import java.util.List;

@SpringBootTest
public class CommentApiV2Test {

    @Autowired
    private CommentRepositoryV2 commentRepository;

    RestClient restClient = RestClient.create("http://localhost:9001");


    @Test
    void create() {
        CommentResponse comment1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse comment2 = create(new CommentCreateRequestV2(1L, "my comment2", comment1.getPath(),1L));
        CommentResponse comment3 = create(new CommentCreateRequestV2(1L, "my comment3", comment2.getPath(), 1L));

        System.out.println("comment1 = " + comment1.getCommentId());
        System.out.println("comment1 = " + comment2.getCommentId());
        System.out.println("comment1 = " + comment3.getCommentId());
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse comment1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentId}", comment1.getCommentId())
                .retrieve()
                .body(CommentResponse.class);

        Assertions.assertThat(response.getCommentId()).isEqualTo(comment1.getCommentId());
    }

    @Test
    void delete() {
        CommentResponse comment1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        restClient.delete()
                .uri("/v2/comments/{commentId}", comment1.getCommentId())
                .retrieve()
                .toBodilessEntity();

        Assertions.assertThat(commentRepository.findById(comment1.getCommentId()).isPresent()).isFalse();
    }


    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=50000")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse response : response1) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }

        String lastPath = response1.getLast().getPath();
        List<CommentResponse> response2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath={lastPath}", lastPath)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse response : response2) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
