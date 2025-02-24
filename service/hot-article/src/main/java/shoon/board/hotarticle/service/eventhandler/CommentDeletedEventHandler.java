package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.CommentDeletedEventPayload;
import shoon.board.hotarticle.repository.ArticleCommentCountRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload>{

    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        CommentDeletedEventPayload payload = event.getPayload();
        articleCommentCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidNight()
        );
    }

    @Override
    public boolean supports(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<CommentDeletedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
