package fr.orion78.blog.engine.admin;

import fr.orion78.blog.engine.Util;
import fr.orion78.blog.engine.content.Content;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Admin {
  private static final Logger LOG = LoggerFactory.getLogger(Admin.class);
  private static final Integer MAX_ATTEMPTS = 5;

  private static Map<String, String> credentials = new HashMap<>();

  static {
    credentials.put("dsa", "root");
  }

  public static Object connectMenu(Request request, Response response) {
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

  public static Object createSessionIfCorrect(Request request, Response response) {
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

    if (Objects.equals(credentials.get(login), password)) {
      LOG.info("Login successful");
      session.attribute("login", login);
      response.redirect("/admin/articles/list");
    } else {
      LOG.info("Login failed");
      response.redirect("/admin/");
    }

    return null;
  }

  public static void ensureSession(Request request, Response response) {
    Session session = request.session();
    if (session == null) {
      response.redirect("/admin/");
      return;
    }

    String login = session.attribute("login");

    if (login == null) {
      response.redirect("/admin/");
      return;
    }

    LOG.info("Valid session for {}", login);
  }

  public static Object getArticleList(Request request, Response response) {
    return "article list";
  }

  public static Object getArticle(Request request, Response response) {
    return null;
  }
}
