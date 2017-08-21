<div class="articleContainer" id="article-${articleId}">
  <#list articleImages as artImg>
    <img src="${artImg}" alt="Article ${articleId} image"/>
  </#list>
  <h1>${articleTitle}</h1>
  <div class="articleSubtitle">${i18n_writtenBy} ${articleAuthor} - ${i18n_lastModified} ${articleDate}</div>
  <div class="articleMd">${articleContent}</div>
  <#if abstractContent>
    <a href="/article/${articleId}">${i18n_seeMore}</a>
    (<span class="disqus-comment-count" data-disqus-identifier="${articleId}">Chargement du nombre de commentaire...</span>)
  <#else>
    <div id="disqus_thread"></div>
    <script>
      var disqus_config = function () {
        this.page.url = "http://iletaittroisfois.orion78.fr/article/${articleId}";
        this.page.identifier = ${articleId};
      };

      (function () {
        var d = document, s = d.createElement('script');
        s.src = 'https://iletaittroisfois.disqus.com/embed.js';
        s.setAttribute('data-timestamp', +new Date());
        (d.head || d.body).appendChild(s);
      })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
  </#if>
</div>