package shoon.board.article.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleWebController {

    @GetMapping("docs/articles")
    public String docs_articles() {
        return "articles.html";
    }
}
