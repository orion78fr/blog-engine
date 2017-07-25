package fr.orion78.blog.engine.content;

import fr.orion78.blog.engine.content.category.Category;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Categories {
  private static final Logger LOG = LoggerFactory.getLogger(Categories.class);

  static volatile List<Category> INSTANCE = Collections.emptyList();

  public static Object getCategory(@NotNull Request request, @NotNull Response response) {
    return null;
  }
}
