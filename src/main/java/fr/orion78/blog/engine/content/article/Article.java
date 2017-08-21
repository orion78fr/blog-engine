package fr.orion78.blog.engine.content.article;

import fr.orion78.blog.engine.Util;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Article {
  private static final Logger LOG = LoggerFactory.getLogger(Article.class);

  private long id;
  private String author;
  private String category;
  private List<String> articleImages;
  private String title;
  private String mdContent;

  private transient long lastModification;

  public Article(long id,
                 @NotNull String author,
                 @NotNull String category,
                 @NotNull List<String> articleImages,
                 @NotNull String title,
                 @NotNull String mdContent) {
    this.id = id;
    this.author = author;
    this.category = category;
    this.articleImages = articleImages;
    this.title = title;
    this.mdContent = mdContent;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public List<String> getArticleImages() {
    return articleImages;
  }

  public void setArticleImages(List<String> articleImages) {
    this.articleImages = articleImages;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMdContent() {
    return mdContent;
  }

  public void setMdContent(String mdContent) {
    this.mdContent = mdContent;
  }

  public long getLastModification() {
    return lastModification;
  }

  public void setLastModification(long lastModification) {
    this.lastModification = lastModification;
  }

  public String getAbstract() {
    if (mdContent == null) {
      return null;
    }

    int idx = Util.ordinalIndexOf(mdContent, "###", 2);

    if (idx == -1) {
      return mdContent;
    } else {
      return mdContent.substring(0, idx);
    }
  }
}
