package fr.orion78.blog.engine;

import fr.orion78.blog.engine.content.Content;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.File;

public class BlogMain {
  private static final Logger LOG = LoggerFactory.getLogger(BlogMain.class);

  public static void main(@NotNull String[] args) {
    if (args.length != 1) {
      LOG.error("No folder given");
      return;
    }
    Content content = Content.reloadContent(new File(args[0]));

    Spark.threadPool(20, 10, 20_000);

    Spark.staticFiles.externalLocation(args[0] + "/static");
    Spark.staticFiles.expireTime(2); // TODO increase it when not in test

    Spark.get("", content::mainContent);
    Spark.get("/", content::mainContent);

    Spark.get("/reload", content::reloadContent);
    Spark.get("/category/*", content::getCategory);
    Spark.get("/article/:article", content::getArticle);

    Spark.path("/admin", () -> {
      Spark.get("", content::adminConnectMenu);
      Spark.get("/", content::adminConnectMenu);

      Spark.post("/connect", content::adminCreateSessionIfCorrect);

      Spark.path("/articles", () -> {
        Spark.before("/*", content::adminEnsureSession);
        Spark.get("/list", content::adminGetArticleList);
        Spark.get("/article/:article", content::adminEditArticle);
      });
    });

    Spark.after((request, response) -> {
          // Only if accepting it
          if (request.headers("Accept-Encoding").contains("gzip")) {
            response.header("Content-Encoding", "gzip");
          }
        }
    );
  }
}
