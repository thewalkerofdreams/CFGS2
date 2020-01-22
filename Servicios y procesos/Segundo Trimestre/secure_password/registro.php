<?php
include "GestionUsuarios.php";

if($_SERVER["REQUEST_METHOD"] == "POST") {
    // username and password sent from form
    $myusername = $_POST['username'];
    $mypassword = $_POST['password'];

    $gestionUsuarios = new GestionUsuarios();//Instanciamos la clase

    if(empty($myusername) == false){
        if($gestionUsuarios->consultarUsuario($myusername) == false){//Si el usuario no existe en la base de datos
            if(empty($mypassword) == false){
                $gestionUsuarios->insertarUsuario($myusername, $mypassword);
                echo 'El usuario se ha insertado correctamente en la base de datos';
            }else{
                echo 'La contraseña se encuentra vacía';
            }
        }else{
            echo 'El usuario ya existe en la base de datos';
        }
    }else{
        echo 'La nombre de usuario se encuentra vacío';
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

            <div style = "font-size:11px; color:#cc0000; margin-top:10px"></div>

</div>

</div>

</div>

</body>
</html>
