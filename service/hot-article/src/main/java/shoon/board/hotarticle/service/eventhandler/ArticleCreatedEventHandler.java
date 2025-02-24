package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.ArticleCreatedEventPayload;
import shoon.board.hotarticle.repository.ArticleCreatedTimeRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload>{

    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        articleCreatedTimeRepository.createOrUpdate(payload.getArticleId(), payload.getCreatedAt(), TimeCalculatorUtils.calculateDurationToMidNight());
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<ArticleCreatedEventPayload> event) {
        return null;
    }
}
