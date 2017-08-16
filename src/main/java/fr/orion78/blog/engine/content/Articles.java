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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Articles {
  private static final Logger LOG = LoggerFactory.getLogger(Articles.class);
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/dd à HH:mm:ss");

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

  private String renderArticle(@NotNull Article article) {
    return renderArticle(article, new StringWriter(), false).toString();
  }

  @NotNull
  Writer renderArticle(@NotNull Article article, @NotNull Writer w, boolean abstractContent) {
    Map<String, Object> mapping = new HashMap<>();
    mapping.put("abstractContent", abstractContent);
    mapping.put("articleId", article.getId());
    mapping.put("articleImage", article.getArticleImage());
    mapping.put("articleTitle", article.getTitle());
    mapping.put("articleContent", abstractContent ? article.getAbstract() : article.getMdContent());
    mapping.put("articleAuthor", article.getAuthor());
    mapping.put("articleDate", LocalDateTime.ofEpochSecond(article.getLastModification() / 1000, 0, ZoneOffset.UTC).format(formatter));

    // TODO
    mapping.put("i18n_writtenBy", "Ecrit par");
    mapping.put("i18n_lastModified", "Modifié le");
    mapping.put("i18n_seeMore", "Lire la suite...");


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
