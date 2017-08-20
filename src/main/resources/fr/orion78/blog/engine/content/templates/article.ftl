<div class="articleContainer" id="article-${articleId}">
  <img src="${articleImage}" alt="Article ${articleId} image"/>
  <h1>${articleTitle}</h1>
  <div class="articleSubtitle">${i18n_writtenBy} ${articleAuthor} - ${i18n_lastModified} ${articleDate}</div>
  <div class="articleMd">${articleContent}</div>
  <#if abstractContent>
    <a href="/article/${articleId}">${i18n_seeMore}</a>
  <!--<script id="dsq-count-scr" src="//iletaittroisfois.disqus.com/count.js" async></script>-->
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