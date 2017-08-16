package fr.orion78.blog.engine.content.article;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Article {
  private static final Logger LOG = LoggerFactory.getLogger(Article.class);

  private long id;
  private String author;
  private String category;
  private String articleImage;
  private String title;
  private String mdContent;

  private transient long lastModification;

  public Article(long id,
                 @NotNull String author,
                 @NotNull String category,
                 @NotNull String articleImage,
                 @NotNull String title,
                 @NotNull String mdContent) {
    this.id = id;
    this.author = author;
    this.category = category;
    this.articleImage = articleImage;
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

  public String getArticleImage() {
    return articleImage;
  }

  public void setArticleImage(String articleImage) {
    this.articleImage = articleImage;
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
}
