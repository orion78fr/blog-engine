package fr.orion78.blog.engine.content;

import fr.orion78.blog.engine.Util;
import fr.orion78.blog.engine.content.article.Article;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Articles {
  private static final Logger LOG = LoggerFactory.getLogger(Articles.class);

  private volatile Map<Long, Article> articles = Collections.emptyMap();

  public Articles(@NotNull Map<Long, Article> articles) {
    this.articles = articles;
  }

  public Map<Long, Article> getArticles() {
    return articles;
  }

  public Object getArticle(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    String articleNum = request.params("article");
    Long l;
    try {
      l = Long.parseLong(articleNum);
    } catch (NumberFormatException e) {
      throw Spark.halt(400, "Article is not a number");
    }

    Article article = articles.get(l);

    if (article == null) {
      throw Spark.halt(404, "Article does not exist");
    }


    Map<String, Object> mapping = new HashMap<>();

    mapping.put("content", renderArticle(article));
    mapping.put("catToHighlight", article.getCategory());

    return content.renderWebpage(mapping);
  }

  private String renderArticle(@NotNull Article article){
    return renderArticle(article, new StringWriter()).toString();
  }

  Writer renderArticle(@NotNull Article article, @NotNull Writer w){
    Map<String, Object> mapping = new HashMap<>();
    mapping.put("articleId", article.getId());
    mapping.put("articleImage", article.getArticleImage());
    mapping.put("articleTitle", article.getTitle());
    mapping.put("articleContent", article.getMdContent());


    Template articleTemplate;
    try {
      articleTemplate = Content.freeMarker.getTemplate("templates/article.ftl");
    } catch (IOException e) {
      throw Spark.halt(404, "Template not found");
    }

    try {
      articleTemplate.process(mapping, w);
    } catch (TemplateException | IOException e) {
      throw Spark.halt(500, "Error in template processing : " + Util.exceptionToStringForWebpage(e));
    }

    return w;
  }
}
