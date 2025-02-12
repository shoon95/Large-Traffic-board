package shoon.board.comment.service.request;

import lombok.Getter;
import shoon.board.comment.service.response.CommentResponse;

import java.util.List;

@Getter
public class CommentPageResponse {
    private List<CommentResponse> comments;
    private Long commentCount;

    public static CommentPageResponse of(List<CommentResponse> comments, Long commentCount) {
        CommentPageResponse response = new CommentPageResponse();
        response.comments = comments;
        response.commentCount = commentCount;
        return response;

    }
}
