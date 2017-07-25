package fr.orion78.blog.engine.content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.orion78.blog.engine.content.category.Category;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {
  private static final Logger LOG = LoggerFactory.getLogger(Content.class);
  private static final Gson gson = new Gson();
  private static File folder;
  private static Configuration freeMarker = new Configuration(new Version(2, 3, 23));

  static {
    freeMarker.setClassForTemplateLoading(Content.class, "");
  }

  public static void reloadContent(@NotNull File folder) {
    Content.folder = folder;

    reloadContent();
  }

  private static void reloadContent() {
    Map<Long, String> articles = new HashMap<>();
    articles.put(1L, "Article 1");
    articles.put(2L, "Article 2");
    articles.put(3L, "Article 3");
    Articles.INSTANCE = articles;

    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(new File(folder, "categories.json")), StandardCharsets.UTF_8);
      Categories.INSTANCE = gson.fromJson(r, new TypeToken<List<Category>>() {
      }.getType());
    } catch (FileNotFoundException e) {
      LOG.error("Category file not found", e);
    }
  }

  public static Object reloadContent(@NotNull Request request, @NotNull Response response) {
    reloadContent();
    response.redirect("/");
    return null;
  }

  public static Object mainContent(@NotNull Request request, @NotNull Response response) {
    StringWriter writer = new StringWriter();
    StringBuilder sb = new StringBuilder();

    sb.append("<ul class=\"nav nav-pills nav-justified\">");
    for (Category category : Categories.INSTANCE) {
      printCat(sb, category, "Accueil");
    }
    sb.append("</ul>");

    String catList = sb.toString();
    Map<String, Object> mapping = new HashMap<>();
    mapping.put("categories", catList);
    mapping.put("title", "Il était trois fois trois meufs");

    Template mainContentTemplate;
    try {
      mainContentTemplate = freeMarker.getTemplate("templates/mainContent.ftl");
    } catch (IOException e) {
      throw Spark.halt(404, "Template not found");
    }

    try {
      mainContentTemplate.process(mapping, writer);
    } catch (TemplateException | IOException e) {
      throw Spark.halt(500, "Error in template processing");
    }

    return writer.toString();
  }

  private static void printCat(@NotNull StringBuilder sb,
                               @NotNull Category category,
                               @Nullable String highlightCat) {
    if (category.getName().equals(highlightCat)) {
      sb.append("<li class=\"active\">");
    } else {
      sb.append("<li>");
    }
    sb.append("<a href=\"/category/").append(category.getName()).append("\">");
    sb.append(category.getName());
    sb.append("</a>");

    if (category.getSubCategories() != null && category.getSubCategories().size() != 0) {
      sb.append("<ul>");
      for (Category subCat : category.getSubCategories()) {
        printCat(sb, subCat, highlightCat);
      }
      sb.append("</ul>");
    }

    sb.append("</li>");
  }
}
