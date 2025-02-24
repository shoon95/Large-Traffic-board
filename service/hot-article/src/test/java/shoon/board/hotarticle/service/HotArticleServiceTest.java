package shoon.board.hotarticle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shoon.board.common.event.Event;
import shoon.board.common.event.EventType;
import shoon.board.hotarticle.client.ArticleClient;
import shoon.board.hotarticle.repository.HotArticleListRepository;
import shoon.board.hotarticle.service.eventhandler.EventHandler;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotArticleServiceTest {


    @InjectMocks
    HotArticleService hotArticleService;

    @Mock
    ArticleClient articleClient;
    @Mock
    List<EventHandler> eventHandlers;
    @Mock
    HotArticleScoreUpdater hotArticleScoreUpdater;
    @Mock
    HotArticleListRepository hotArticleListRepository;

    @Test
    void handleEventIfEventHandlerNotFoundTest() {
        // given
        Event event = mock(Event.class);
        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(false);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then

        verify(eventHandler, never()).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
    }

    @Test
    void handleEventIfArticleCreatedEvenTest() {
        // given
        Event event = mock(Event.class);
        given(event.getType()).willReturn(EventType.ARTICLE_CREATED);

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then

        verify(eventHandler).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
    }

    @Test
    void handleEventIfArticleDeletedEvenTest() {
        // given
        Event event = mock(Event.class);
        given(event.getType()).willReturn(EventType.ARTICLE_DELETED);

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then

        verify(eventHandler).handle(event);
        verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
    }

    @Test
    void handleEventIfScoreUpdatableEventTest() {
        // given
        Event event = mock(Event.class);
        given(event.getType()).willReturn(mock(EventType.class));

        EventHandler eventHandler = mock(EventHandler.class);
        given(eventHandler.supports(event)).willReturn(true);
        given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

        // when
        hotArticleService.handleEvent(event);

        // then

        verify(eventHandler, never()).handle(event);
        verify(hotArticleScoreUpdater).update(event, eventHandler);
    }
}