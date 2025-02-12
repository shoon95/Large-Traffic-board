package shoon.board.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shoon.board.comment.entity.Comment;
import shoon.board.comment.service.CommentService;
import shoon.board.comment.service.request.CommentCreateRequest;
import shoon.board.comment.service.request.CommentPageResponse;
import shoon.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.read(commentId);
    }

    // create
    @PostMapping("/v1/comments")
    public CommentResponse create(
            @RequestBody CommentCreateRequest request
    ) {
        return commentService.create(request);
    }


    // delete
    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(
            @PathVariable("commentId") Long commentId
    ) {
        commentService.delete(commentId);
    }

    @GetMapping("/v1/comments")
    public CommentPageResponse readAll(
            @RequestParam("articleId") Long articleId,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/v1/comments/infinite_scroll")
    public List<CommentResponse> readAll(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
            @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
            @RequestParam("pageSize") Long pageSize
    ) {
        return commentService.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);
    }

}
