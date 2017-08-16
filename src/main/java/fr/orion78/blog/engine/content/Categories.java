package fr.orion78.blog.engine.content;

import fr.orion78.blog.engine.content.article.Article;
import fr.orion78.blog.engine.content.category.Category;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories {
  private static final Logger LOG = LoggerFactory.getLogger(Categories.class);

  private volatile List<Category> categories;
  private volatile Map<String, Category> categoryMap = new HashMap<>();

  public Categories(@NotNull List<Category> categories) {
    this.categories = categories;

    fixFullPath();
  }

  public List<Category> getCategories() {
    return categories;
  }

  public Map<String, Category> getCategoryMap() {
    return categoryMap;
  }

  Object getCategory(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    String[] splat = request.splat();
    if (splat.length != 1) {
      throw Spark.halt(400, "No category given");
    }

    String category = splat[0];

    Category cat = categoryMap.get(category);

    if (cat == null) {
      throw Spark.halt(404, "Category not found");
    }

    Map<String, Object> mapping = new HashMap<>();
    mapping.put("catToHighlight", category);
    if (cat.getArticles().size() == 0) {
      mapping.put("content", "This category " + category + " is empty !");
    } else {
      StringWriter w = new StringWriter();
      for (Article article : cat.getArticles()) {
        content.getArticles().renderArticle(article, w, true);
      }
      mapping.put("content", w.toString());
    }

    return content.renderWebpage(mapping);
  }

  private void fixFullPath() {
    for (Category cat : categories) {
      fixFullPath(cat, "");
    }
  }

  private void fixFullPath(@NotNull Category cat, @NotNull String s) {
    cat.setFullPath(s + cat.getName());
    categoryMap.put(cat.getFullPath(), cat);

    for (Category subCat : cat.getSubCategories()) {
      fixFullPath(subCat, cat.getFullPath() + '/');
    }
  }
}
