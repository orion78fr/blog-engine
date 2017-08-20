package fr.orion78.blog.engine.admin;

import fr.orion78.blog.engine.Util;
import fr.orion78.blog.engine.content.Content;
import fr.orion78.blog.engine.content.article.Article;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Admin {
  private static final Logger LOG = LoggerFactory.getLogger(Admin.class);
  private static final Integer MAX_ATTEMPTS = 5;

  private Map<String, String> credentials;

  public Admin(Map<String, String> credentials) {
    this.credentials = credentials;
  }

  public Object connectMenu(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    StringWriter writer = new StringWriter();

    Map<String, Object> mapping = new HashMap<>();

    Template mainContentTemplate;
    try {
      mainContentTemplate = Content.freeMarker.getTemplate("templates/adminConnect.ftl");
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

  public Object createSessionIfCorrect(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    Session session = request.session(true);
    session.maxInactiveInterval(600);

    String login = request.queryParams("login");
    String password = request.queryParams("passwd");
    LOG.info("Login : {} Password : {}", login, password);

    Integer attempts = session.attribute("attempts");
    if (attempts == null) {
      attempts = 1;
    } else {
      attempts += 1;
    }

    if (attempts > MAX_ATTEMPTS) {
      LOG.info("Too many attempts");
      response.redirect("/admin/");
      return null;
    }

    session.attribute("attempts", attempts);

    if (login != null && password != null && Objects.equals(credentials.get(login), password)) {
      LOG.info("Login successful");
      session.attribute("login", login);
      response.redirect("/admin/articles/list");
    } else {
      LOG.info("Login failed");
      response.redirect("/admin/");
    }

    return null;
  }

  private String passwdSha1(String passwd) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.reset();
    md.update(passwd.getBytes(StandardCharsets.UTF_8));
    byte[] digest = md.digest();

    try(Formatter f = new Formatter()){
      return f.format("%02x", digest).toString();
    }
  }

  public void ensureSession(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    Session session = request.session();
    if (session == null) {
      response.redirect("/admin");
      return;
    }

    String login = session.attribute("login");

    if (login == null) {
      response.redirect("/admin");
      return;
    }

    LOG.debug("Valid session for {}", login);
  }

  public Object getArticleList(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    StringWriter w = new StringWriter();
    content.getArticles().getArticles().entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getKey(), a.getKey()))
        .forEachOrdered((e) -> {
          Article art = e.getValue();
          w.append("<a href=\"/admin/articles/article/")
              .append(Long.toString(art.getId()))
              .append("\">")
              .append(art.getTitle())
              .append("</a><br/>");
        });
    return w.toString();
  }

  public Object editArticle(@NotNull Content content, @NotNull Request request, @NotNull Response response) {
    String articleNum = request.params("article");
    Long l;
    try {
      l = Long.parseLong(articleNum);
    } catch (NumberFormatException e) {
      throw Spark.halt(400, "Article is not a number");
    }

    Article article = content.getArticles().getArticles().get(l);

    if (article == null) {
      throw Spark.halt(404, "Article does not exist");
    }

    StringWriter writer = new StringWriter();

    Map<String, Object> mapping = new HashMap<>();
    mapping.put("articleMd", article.getMdContent());

    Template mainContentTemplate;
    try {
      mainContentTemplate = Content.freeMarker.getTemplate("templates/editArticle.ftl");
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
}
