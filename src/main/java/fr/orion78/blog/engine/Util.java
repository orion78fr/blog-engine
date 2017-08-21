package fr.orion78.blog.engine;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Util {
  private static final Logger LOG = LoggerFactory.getLogger(Util.class);
  private static final int MAX_DEEP_STACKTRACE = 3;

  private Util() {
    throw new IllegalStateException();
  }

  @NotNull
  public static String exceptionToStringForWebpage(@NotNull Throwable t) {
    return exceptionToString(t).replace("\n", "<br />");
  }

  @NotNull
  public static String exceptionToString(@NotNull Throwable t) {
    return fullStackTrace(t, 0);
  }

  @NotNull
  private static String fullStackTrace(@NotNull Throwable t, int deep) {
    StringBuilder sb = new StringBuilder();

    sb.append(t.getClass().getCanonicalName())
        .append(" : ")
        .append(t.getMessage())
        .append("\n");

    StackTraceElement[] stes = t.getStackTrace();

    for (StackTraceElement ste : stes) {
      sb.append("\tat ")
          .append(ste.getClassName())
          .append(".")
          .append(ste.getMethodName())
          .append("(")
          .append(ste.getFileName())
          .append(":")
          .append(ste.getLineNumber())
          .append(")");
    }

    if (t.getCause() != null) {
      sb.append("\n");
      if (deep < MAX_DEEP_STACKTRACE) {
        sb.append("Cause is :")
            .append(fullStackTrace(t.getCause(), deep + 1));
      } else {
        sb.append("More nested causes (max is ")
            .append(MAX_DEEP_STACKTRACE)
            .append(")");
      }
    }

    return sb.toString();
  }

  public static int ordinalIndexOf(String str, String substr, int n) {
    int pos = str.indexOf(substr);
    while (--n > 0 && pos != -1)
      pos = str.indexOf(substr, pos + 1);
    return pos;
  }
}
