package fr.orion78.blog.engine.content.category;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class Category {
  private static final Logger LOG = LoggerFactory.getLogger(Category.class);

  private String name;
  private List<Category> subCategories;
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
}
