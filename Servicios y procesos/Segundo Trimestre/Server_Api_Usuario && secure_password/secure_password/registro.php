<?php
include("config.php");
include ("tabla1.php");
session_start();

if($_SERVER["REQUEST_METHOD"] == "POST") {
    if(!empty(username) && !empty(password)){//Si el usuario y la contraseña no estan vacíos
        $myusername = mysqli_real_escape_string($db,$_POST['username']);
        $mypassword = mysqli_real_escape_string($db,$_POST['password']);

        $sql = "INSERT INTO ". \Constantes_DB\tabla1::TABLE_NAME ." (".\Constantes_DB\tabla1::NAME.", ".\Constantes_DB\tabla1::HASHKEY.")
        VALUES (".$myusername.", ".$mypassword.")";
        $result = mysqli_query($db,$sql);
        $row = mysqli_fetch_array($result,MYSQLI_ASSOC);
        $active = $row['active'];

        $count = mysqli_num_rows($result);

        // If result matched $myusername and $mypassword, table row must be 1 row
        if($count == 1) {
            $mensaje = "Cuenta creada";
        }else {
            $error = "Your Login Name or Password is invalid";
        }
    }
}
?>
<html>

<head>
    <title>Login Page</title>

    <style type = "text/css">
        body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:14px;
        }
        label {
            font-weight:bold;
            width:100px;
            font-size:14px;
        }
        .box {
            border:#666666 solid 1px;
        }
    </style>

</head>

<body bgcolor = "#FFFFFF">

<div align = "center">
    <div style = "width:300px; border: solid 1px #333333; " align = "left">
        <div style = "background-color:#333333; color:#FFFFFF; padding:3px;"><b>Registrar usuario</b></div>

        <div style = "margin:30px">

            <form action = "" method = "post">
                <label>UserName  :</label><input type = "text" name = "username" class = "box"/><br /><br />
                <label>Password  :</label><input type = "password" name = "password" class = "box" /><br/><br />
                <input type = "submit" value = " Submit "/><br />
            </form>

            <p>$mensaje</p>

            <div style = "font-size:11px; color:#cc0000; margin-top:10px"><?php echo $error; ?></div>

</div>

</div>

</div>

</body>
</html>
