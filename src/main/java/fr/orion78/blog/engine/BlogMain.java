package fr.orion78.blog.engine;

import fr.orion78.blog.engine.admin.Admin;
import fr.orion78.blog.engine.content.Articles;
import fr.orion78.blog.engine.content.Categories;
import fr.orion78.blog.engine.content.Content;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.File;

public class BlogMain {
  private static final Logger LOG = LoggerFactory.getLogger(BlogMain.class);

  public static void main(@NotNull String[] args) {
    Content.reloadContent(new File(System.getProperty("user.home") + "/www/blogs/meuf"));

    Spark.threadPool(20, 10, 20_000);

    Spark.staticFiles.externalLocation(System.getProperty("user.home") + "/www/blogs/meuf/static");
    Spark.staticFiles.expireTime(600);

    Spark.get("/", Content::mainContent);
    Spark.get("/reload", Content::reloadContent);
    Spark.get("/category/:category", Categories::getCategory);
    Spark.get("/article/:article", Articles::getArticle);
    Spark.get("/admin", Admin::adminMenu);

    Spark.after((request, response) -> response.header("Content-Encoding", "gzip"));
  }
}
