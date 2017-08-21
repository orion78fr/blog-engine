package fr.orion78.blog.engine.content.category;

import fr.orion78.blog.engine.content.article.Article;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Category {
  private static final Logger LOG = LoggerFactory.getLogger(Category.class);

  private String name;
  private List<Category> subCategories;
  private transient TreeSet<Article> articles;
  private transient String fullPath;

  public Category(@NotNull String name, @Nullable List<Category> subCategories) {
    this.name = name;
    this.subCategories = subCategories == null ? Collections.emptyList() : subCategories;
  }

  public String getName() {
    return name;
  }

  @NotNull
  public List<Category> getSubCategories() {
    return subCategories == null ? Collections.emptyList() : subCategories;
  }

  public String getFullPath() {
    return fullPath;
  }

  public void setFullPath(String fullPath) {
    this.fullPath = fullPath;
  }

  private void checkInit() {
    if (articles == null) {
      articles = new TreeSet<>(Comparator.comparing(Article::getId).reversed());
    }
  }

  public void addArticle(@NotNull Article article) {
    checkInit();
    articles.add(article);
  }

  public TreeSet<Article> getArticles() {
    checkInit();
    return articles;
  }

  @Nullable
  public Category findSubcat(@NotNull String cat) {
    for (Category subCategory : subCategories) {
      if(subCategory.getName().equals(cat)){
        return subCategory;
      }
    }
    return null;
  }
}
