package fr.orion78.blog.engine.content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.orion78.blog.engine.Util;
import fr.orion78.blog.engine.content.article.Article;
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
  static final Gson gson = new Gson();
  static Configuration freeMarker = new Configuration(new Version(2, 3, 23));
  static {
    freeMarker.setClassForTemplateLoading(Content.class, "");
  }

  private File staticFilesFolder;
  private Articles articles;
  private Categories categories;
  private String blogTitle;
  private String blogTitleImg;
  private String sidebarMd;

  public Content(File staticFilesFolder) {
    this.staticFilesFolder = staticFilesFolder;
  }

  public static Content reloadContent(@NotNull File folder) {
    Content content = new Content(folder);
    content.reloadContent();
    return content;
  }

  private void reloadContent() {
    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(new File(staticFilesFolder, "common.json")), StandardCharsets.UTF_8);
      Map<String, Object> common = gson.fromJson(r, new TypeToken<Map<String, Object>>() {
      }.getType());

      this.blogTitle = (String) common.get("title");
      this.blogTitleImg = (String) common.get("titleImg");
      this.sidebarMd = (String) common.get("sidebarMd");
    } catch (FileNotFoundException e) {
      LOG.error("Common file not found", e);
    }

    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(new File(staticFilesFolder, "categories.json")), StandardCharsets.UTF_8);
      List<Category> categories = gson.fromJson(r, new TypeToken<List<Category>>() {
      }.getType());

      this.categories = new Categories(categories);
    } catch (FileNotFoundException e) {
      LOG.error("Category file not found", e);
    }

    Map<Long, Article> articles = new HashMap<>();
    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(new File(staticFilesFolder, "articles/article-2.json")), StandardCharsets.UTF_8);
      Article article = gson.fromJson(r, Article.class);
      categories.getCategoryMap().get(article.getCategory()).addArticle(article);
      articles.put(2L, article);
    } catch (FileNotFoundException e) {
      LOG.error("Articles file not found", e);
    }
    this.articles = new Articles(articles);
  }

  public Object reloadContent(@NotNull Request request, @NotNull Response response) {
    reloadContent();
    response.redirect("/");
    return null;
  }

  public Object mainContent(@NotNull Request request, @NotNull Response response) {
    response.redirect("/category/" + this.categories.getCategories().get(0).getFullPath());
    return null;
  }

  @NotNull
  private String getCatList(@NotNull String catToHighlight) {
    StringBuilder sb = new StringBuilder();

    sb.append("<ul class=\"nav nav-pills nav-justified\">");
    for (Category category : categories.getCategories()) {
      printCat(sb, category, catToHighlight);
    }
    sb.append("</ul>");

    return sb.toString();
  }

  @NotNull
  String renderWebpage(@NotNull Map<String, Object> mapping) {
    StringWriter writer = new StringWriter();

    mapping.put("categories", getCatList((String) mapping.get("catToHighlight")));
    mapping.put("title", blogTitle);

    Template mainContentTemplate;
    try {
      mainContentTemplate = freeMarker.getTemplate("templates/mainContent.ftl");
    } catch (IOException e) {
      throw Spark.halt(404, "Template not found");
    }

    try {
      mainContentTemplate.process(mapping, writer);
    } catch (TemplateException | IOException e) {
      throw Spark.halt(500, "Error in template processing : " + Util.exceptionToStringForWebpage(e));
    }

    return writer.toString();
  }

  private static void printCat(@NotNull StringBuilder sb,
                               @NotNull Category category,
                               @Nullable String highlightCat) {
    if (category.getFullPath().equals(highlightCat)) {
      sb.append("<li class=\"active\">");
    } else {
      sb.append("<li>");
    }
    sb.append("<a href=\"/category/").append(category.getFullPath()).append("\">");
    sb.append(category.getName());
    sb.append("</a>");

    if (category.getSubCategories().size() != 0) {
      sb.append("<ul>");
      for (Category subCat : category.getSubCategories()) {
        printCat(sb, subCat, highlightCat);
      }
      sb.append("</ul>");
    }

    sb.append("</li>");
  }

  public Object getCategory(@NotNull Request request, @NotNull Response response) {
    return categories.getCategory(this, request, response);
  }

  public Object getArticle(@NotNull Request request, @NotNull Response response) {
    return articles.getArticle(this, request, response);
  }
}
