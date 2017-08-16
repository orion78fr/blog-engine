<div class="articleContainer" id="article-${articleId}">
  <img src="${articleImage}" alt="Article ${articleId} image"/>
  <h1>${articleTitle}</h1>
  <div class="articleSubtitle">${i18n_writtenBy} ${articleAuthor} - ${i18n_lastModified} ${articleDate}</div>
  <div class="articleMd">${articleContent}</div>
  <#if abstractContent>
    <a href="/article/${articleId}">${i18n_seeMore}</a>
  </#if>
</div>