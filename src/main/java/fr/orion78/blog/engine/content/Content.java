package fr.orion78.blog.engine.content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.orion78.blog.engine.Util;
import fr.orion78.blog.engine.admin.Admin;
import fr.orion78.blog.engine.content.article.Article;
import fr.orion78.blog.engine.content.category.Category;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.jetbrains.annotations.NotNull;
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
  public static final Gson gson = new Gson();
  public static final Configuration freeMarker = new Configuration(new Version(2, 3, 23));

  static {
    freeMarker.setClassForTemplateLoading(Content.class, "");
  }

  private File staticFilesFolder;
  private Articles articles;
  private Categories categories;
  private Admin admin;
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
    File folder = new File(staticFilesFolder, "articles");
    File[] files = folder.listFiles();
    if (files == null) {
      LOG.error("Cannot find articles !");
    } else {
      for (File article : files) {
        loadArticle(articles, article);
      }

      this.articles = new Articles(articles);
    }

    Map<String, String> credentials = new HashMap<>();
    credentials.put("dsa", "root");
    this.admin = new Admin(credentials);
  }

  private void loadArticle(@NotNull Map<Long, Article> articles, @NotNull File article) {
    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(article), StandardCharsets.UTF_8);
      Article art = gson.fromJson(r, Article.class);
      art.setLastModification(article.lastModified());
      categories.getCategoryMap().get(art.getCategory()).addArticle(art);
      categories.getCategories().get(0).addArticle(art); // Add article to home too
      articles.put(art.getId(), art);
    } catch (FileNotFoundException e) {
      LOG.error("Articles file not found", e);
    }
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
  String renderWebpage(@NotNull Map<String, Object> mapping) {
    StringWriter writer = new StringWriter();

    mapping.put("categories", categories.getCategories());
    mapping.put("title", blogTitle);
    mapping.put("sidebarMd", sidebarMd);

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

  public Object getCategory(@NotNull Request request, @NotNull Response response) {
    return categories.getCategory(this, request, response);
  }

  public Object getArticle(@NotNull Request request, @NotNull Response response) {
    return articles.getArticle(this, request, response);
  }

  public Object adminConnectMenu(@NotNull Request request, @NotNull Response response) {
    return admin.connectMenu(this, request, response);
  }

  public Object adminCreateSessionIfCorrect(@NotNull Request request, @NotNull Response response) {
    return admin.createSessionIfCorrect(this, request, response);
  }

  public void adminEnsureSession(@NotNull Request request, @NotNull Response response) {
    admin.ensureSession(this, request, response);
  }

  public Object adminGetArticleList(@NotNull Request request, @NotNull Response response) {
    return admin.getArticleList(this, request, response);
  }

  public Object adminEditArticle(@NotNull Request request, @NotNull Response response) {
    return admin.editArticle(this, request, response);
  }

  public Articles getArticles() {
    return articles;
  }

  public Categories getCategories() {
    return categories;
  }

  String getBlogTitle() {
    return blogTitle;
  }

  String getBlogTitleImg() {
    return blogTitleImg;
  }

  String getSidebarMd() {
    return sidebarMd;
  }
}
