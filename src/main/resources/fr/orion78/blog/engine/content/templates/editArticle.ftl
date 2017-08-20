<!DOCTYPE html>
<html>
  <head>
    <title>My first Vue app</title>
    <link rel="stylesheet" type="text/css" href="/css/adminEditArticle.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/vue/2.3.4/vue.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/marked/0.3.6/marked.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  </head>
  <body>
    <div id="editor">
      <div id="default-value" style="display: none">${articleMd}</div>
      <textarea :value="input" @input="update"></textarea>
      <div v-html="compiledMarkdown"></div>
    </div>

    <script>
      new Vue({
        el: '#editor',
        data: {
          input: $("#default-value").text()
        },
        computed: {
          compiledMarkdown: function () {
            return marked(this.input, { sanitize: false })
          }
        },
        methods: {
          update: _.debounce(function (e) {
            this.input = e.target.value
          }, 300)
        }
      });
    </script>
  </body>
</html>