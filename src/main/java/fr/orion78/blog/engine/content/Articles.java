package fr.orion78.blog.engine.content;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collections;
import java.util.Map;

public class Articles {
  private static final Logger LOG = LoggerFactory.getLogger(Articles.class);

  static volatile Map<Long, String> INSTANCE = Collections.emptyMap();

  public static Object getArticle(@NotNull Request request, @NotNull Response response) {
    String articleNum = request.params("article");
    Long l;
    try {
      l = Long.parseLong(articleNum);
    } catch (NumberFormatException e) {
      throw Spark.halt(400, "Article is not a number");
    }

    String article = INSTANCE.get(l);

    if (article == null) {
      throw Spark.halt(404, "Article does not exist");
    }

    return article;
  }
}
