package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.ArticleLikedEventPayload;
import shoon.board.hotarticle.repository.ArticleLikeCountRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPayload>{

    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Override
    public void handle(Event<ArticleLikedEventPayload> event) {
        ArticleLikedEventPayload payload = event.getPayload();
        articleLikeCountRepository.createOrUpdate(payload.getArticleId(), payload.getArticleLikeCount(), TimeCalculatorUtils.calculateDurationToMidNight());
    }

    @Override
    public boolean supports(Event<ArticleLikedEventPayload> event) {
        return EventType.ARTICLE_LIKED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<ArticleLikedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
