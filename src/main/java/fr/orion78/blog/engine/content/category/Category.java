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

  public Category(@NotNull String name, @Nullable List<Category> subCategories) {
    this.name = name;
    this.subCategories = subCategories == null ? Collections.emptyList() : subCategories;
  }

  public String getName() {
    return name;
  }

  public List<Category> getSubCategories() {
    return subCategories;
  }
}
