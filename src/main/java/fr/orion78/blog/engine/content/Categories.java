package fr.orion78.blog.engine.content;

import fr.orion78.blog.engine.content.category.Category;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories {
  private static final Logger LOG = LoggerFactory.getLogger(Categories.class);

  private volatile List<Category> categories = Collections.emptyList();

  public Categories(@NotNull List<Category> categories) {
    this.categories = categories;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public Object getCategory(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    String[] splat = request.splat();
    if (splat.length != 1) {
      throw Spark.halt(400, "No category given");
    }

    String category = splat[0];

    Map<String, Object> mapping = new HashMap<>();
    mapping.put("content", "This is the content of the category " + category);
    mapping.put("catToHighlight", category);

    return content.renderWebpage(mapping);
  }

  public static void fixFullPath(@NotNull List<Category> categories) {
    for (Category cat : categories) {
      fixFullPath(cat, "");
    }
  }

  private static void fixFullPath(@NotNull Category cat, @NotNull String s) {
    cat.setFullPath(s + cat.getName());

    for (Category subCat : cat.getSubCategories()) {
      fixFullPath(subCat, cat.getFullPath() + '/');
    }
  }
}
