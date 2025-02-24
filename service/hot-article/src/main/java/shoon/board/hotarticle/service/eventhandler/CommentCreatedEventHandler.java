package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.CommentCreatedEventPayload;
import shoon.board.hotarticle.repository.ArticleCommentCountRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload>{

    private final ArticleCommentCountRepository articleCommentCountRepository;
    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        CommentCreatedEventPayload payload = event.getPayload();
        articleCommentCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidNight()
        );
    }

    @Override
    public boolean supports(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<CommentCreatedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
