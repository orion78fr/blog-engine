<!DOCTYPE html>
<html>
  <head>
    <title>Admin menu</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
    <link rel="stylesheet" href="/css/adminConnect.css"/>
  </head>

  <body>
    <div id="loginForm">
      <div class="tableDiv">
        <form action="connect" method="post">
          <div id="loginDiv">
            <label for="formLogin">Login : </label> <input id="formLogin" name="login" type="text" formtarget="login"/>
          </div>
          <div id="passwdDiv">
            <label for="formPasswd">Password : </label> <input id="formPasswd" name="passwd" type="password"/>
          </div>
          <input type="submit" value="Log in"/>
        </form>
      </div>
    </div>
  </body>
</html>