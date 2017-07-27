package fr.orion78.blog.engine.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Admin {
  private static final Logger LOG = LoggerFactory.getLogger(Admin.class);

  public static Object adminMenu(Request request, Response response) {
    throw Spark.halt(404, "Currently not implemented");
  }
}
