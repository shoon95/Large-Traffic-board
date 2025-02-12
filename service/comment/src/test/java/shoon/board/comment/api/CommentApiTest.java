package shoon.board.comment.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import shoon.board.comment.entity.Comment;
import shoon.board.comment.repository.CommentRepository;
import shoon.board.comment.service.request.CommentPageResponse;
import shoon.board.comment.service.response.CommentResponse;

import java.util.List;

@SpringBootTest
public class CommentApiTest {


    RestClient restClient = RestClient.create("http://localhost:9001");

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

        Assertions.assertThat(response1.getCommentId()).isEqualTo(response2.getParentCommentId()).isEqualTo(response3.getParentCommentId());
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse parentComment = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));

        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", parentComment.getCommentId())
                .retrieve()
                .body(CommentResponse.class);

        Assertions.assertThat(response.getCommentId()).isEqualTo(parentComment.getCommentId());

    }

    @Test
    void delete() throws InterruptedException {

        CommentResponse parentComment = createComment(new CommentCreateRequest(1L, "my comment10", null, 1L));
        CommentResponse childComment = createComment(new CommentCreateRequest(1L, "my comment11", parentComment.getCommentId(), 1L));


        commentDelete(parentComment.getCommentId());

        Assertions.assertThat(commentRepository.findById(parentComment.getCommentId())
                .map(Comment::getDeleted)
                .orElse(false))
                .isTrue();
//
        commentDelete(childComment.getCommentId());

        Assertions.assertThat(commentRepository.existsById(parentComment.getCommentId())).isFalse();
        Assertions.assertThat(commentRepository.existsById(childComment.getCommentId())).isFalse();
    }

    void commentDelete(Long commentId) {
        restClient.delete()
                .uri("/v1/comments/{commentId}", commentId)
                .retrieve()
                .toBodilessEntity();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);

        List<CommentResponse> comments = response.getComments();
        for (CommentResponse comment : comments) {
            System.out.println("comment.getContent() = " + comment.getContent());
        }

    }

    @AllArgsConstructor
    @Getter
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}
