package shoon.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventPayload;
import shoon.board.common.event.EventType;
import shoon.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {

    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.SHOON_BOARD_ARTICLE,
            EventType.Topic.SHOON_BOARD_COMMENT,
            EventType.Topic.SHOON_BOARD_LIKE,
            EventType.Topic.SHOON_BOARD_VIEW,
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
