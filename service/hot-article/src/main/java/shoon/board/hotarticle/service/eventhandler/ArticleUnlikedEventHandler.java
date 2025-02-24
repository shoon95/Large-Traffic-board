package shoon.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.common.event.payload.ArticleUnlikedEventPayload;
import shoon.board.hotarticle.repository.ArticleLikeCountRepository;
import shoon.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload>{

    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        ArticleUnlikedEventPayload payload = event.getPayload();
        articleLikeCountRepository.createOrUpdate(
                payload.getArticleId(), payload.getArticleLikeCount(), TimeCalculatorUtils.calculateDurationToMidNight()
        );
    }

    @Override
    public boolean supports(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
