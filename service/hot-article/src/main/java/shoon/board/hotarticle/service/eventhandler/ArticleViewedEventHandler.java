package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.ArticleViewedEventPayload;
import shoon.board.hotarticle.repository.ArticleViewCountRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload>{

    private final ArticleViewCountRepository articleViewCountRepository;
    @Override
    public void handle(Event<ArticleViewedEventPayload> event) {
        ArticleViewedEventPayload payload = event.getPayload();
        articleViewCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleViewCount(),
                TimeCalculatorUtils.calculateDurationToMidNight()
        );
    }

    @Override
    public boolean supports(Event<ArticleViewedEventPayload> event) {
        return EventType.ARTICLE_VIEWED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<ArticleViewedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
