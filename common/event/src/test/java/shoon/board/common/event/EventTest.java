package shoon.board.common.event;

import net.bytebuddy.asm.Advice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import shoon.board.common.event.payload.ArticleCreatedEventPayload;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void serde() {
        // given
        ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .boardArticleCount(23L)
                .build();

        Event<EventPayload> event = Event.of(
                1234L,
                EventType.ARTICLE_CREATED,
                payload
        );

        String json = event.toJson();
        System.out.println("json = " + json);

        // when
        Event<EventPayload> result = Event.fromJson(json);

        // then
        assertThat(result.getEventId()).isEqualTo(event.getEventId());
        assertThat(result.getType()).isEqualTo(event.getType());
        assertThat(result.getPayload()).isInstanceOf(ArticleCreatedEventPayload.class);

        ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload) result.getPayload();
        assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
        assertThat(resultPayload.getTitle()).isEqualTo(payload.getTitle());
        assertThat(resultPayload.getCreatedAt()).isEqualTo(payload.getCreatedAt());
    }
}