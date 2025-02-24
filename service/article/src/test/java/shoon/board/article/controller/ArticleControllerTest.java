package shoon.board.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shoon.board.article.entity.Article;
import shoon.board.article.service.ArticleService;
import shoon.board.article.service.response.ArticleResponse;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    ArticleService articleService;


    @Test
    void readArticleTest() throws Exception {

        // given
        Long articleId = 1L;

        Article article = Article.create(1L, "테스트 제목", "테스트 내용", 2L, 2L);
        ArticleResponse articleResponse = ArticleResponse.from(article);

        BDDMockito.given(articleService.read(articleId)).willReturn(articleResponse);

        // when
        ResultActions result = mockMvc.perform(get("/v1/articles/{articleId}", articleId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("get-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("articleId").description("조회할 게시글의 ID")
                    ),
                    responseFields(
                            fieldWithPath("articleId").description("조회한 게시글 ID").type(JsonFieldType.NUMBER),
                            fieldWithPath("title").description("조회한 게시글 제목").type(JsonFieldType.STRING),
                            fieldWithPath("content").description("조회한 게시글 내용").type(JsonFieldType.STRING),
                            fieldWithPath("boardId").description("조회한 게시글의 보드 ID").type(JsonFieldType.NUMBER),
                            fieldWithPath("writerId").description("조회한 게시글 작성자 ID").type(JsonFieldType.NUMBER),
                            fieldWithPath("createdAt").description("조회한 게시글 작성 시간").type(JsonFieldType.STRING),
                            fieldWithPath("modifiedAt").description("조회한 게시글 수정 시간").type(JsonFieldType.STRING)
                    )
            ));
    }
}
