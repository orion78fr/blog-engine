<!DOCTYPE html>
<html>
  <head>
    <title>${title}</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
    <link href="https://fonts.googleapis.com/css?family=Miss+Fajardose" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css"/>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/marked/0.3.6/marked.min.js"
            integrity="sha256-mJAzKDq6kSoKqZKnA6UNLtPaIj8zT2mFnWu/GSouhgQ="
            crossorigin="anonymous"></script>
  </head>

  <body>
    <header class="page-header">
      ${title}
    </header>
    <nav>
      ${categories}
    </nav>

    <div class="content">
      <div class="mainContent col-md-10">
        ${content}
      </div>
      <div class="sidebar col-md-2">
        <div class="googleSearch">
          This is where the google search bar will be.
        </div>
        <div class="sidebarMd">${sidebarMd}</div>
      </div>
    </div>

    <script>
      $(".articleMd, .sidebarMd").each(function () {
        var container = $(this);
        container.html(marked(container.html(), {sanitize: false}));
      })
    </script>
  </body>
</html>