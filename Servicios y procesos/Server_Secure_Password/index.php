<?php

require_once "Request.php";
require_once "Response.php";
require_once "model/Autentication.php";
require_once "controller/NotPermissionController.php";

//In PHP __autoload() is a magic method, means it calls automatically when you try create an object of the class and ç
//if the PHP engine doesn't find the class in the script it'll try to call __autoload() magic method.
//Autoload rules
spl_autoload_register('apiAutoload');//Registrar las funciones dadas como implementación de __autoload()
function apiAutoload($classname)
{
    $res = false;

    //If the class name ends in "Controller", then try to locate the class in the controller directory to include it (require_once)
    if (preg_match('/[a-zA-Z]+Controller$/', $classname)) {
        if (file_exists(__DIR__ . '/controller/' . $classname . '.php')) {
//            echo "cargamos clase: " . __DIR__ . '/controller/' . $classname . '.php';
            require_once __DIR__ . '/controller/' . $classname . '.php';
            $res = true;
        }
    } elseif (preg_match('/[a-zA-Z]+Model$/', $classname)) {
        if (file_exists(__DIR__ . '/model/' . $classname . '.php')) {
//            echo "<br/>cargamos clase: " . __DIR__ . '/model/' . $classname . '.php';
            require_once __DIR__ . '/model/' . $classname . '.php';
//            echo "clase cargada.......................";
            $res = true;
        }
    }
    //Instead of having Views, like in a Model-View-Controller project,
    //we will have a Response class. So we don't need the following.
    //Although we could have different classes to generate the output,
    //for example: JsonView, XmlView, HtmlView... I think in our case
    //it will be better to have asingle class to generate the output (Response class)
    //elseif (preg_match('/[a-zA-Z]+View$/', $classname)) {
    //    require_once __DIR__ . '/views/' . $classname . '.php';
    //    $res = true;
    //}
    return $res;
}


//Let's retrieve all the information from the request
$verb = $_SERVER['REQUEST_METHOD']; //$_SERVER --> Información del entorno del servidor y de ejecución
//IMPORTANT: WITH CGI OR FASTCGI, PATH_INFO WILL NOT BE AVAILABLE!!!
//SO WE NEED FPM OR PHP AS APACHE MODULE (UNSECURE, DEPRECATED) INSTEAD OF CGI OR FASTCGI
$path_info = !empty($_SERVER['PATH_INFO']) ? $_SERVER['PATH_INFO'] : (!empty($_SERVER['ORIG_PATH_INFO']) ? $_SERVER['ORIG_PATH_INFO'] : '');
$url_elements = explode('/', $path_info);//Como la función split de Java

$query_string = null;
if (isset($_SERVER['QUERY_STRING'])) {//Si la consulta no es nula
    parse_str($_SERVER['QUERY_STRING'], $query_string);//Pasamos el elemento 'QUERY_STRING' a string y lo almacenamos en $query_string
}
$body = file_get_contents("php://input");//Transmite un fichero completo a una cadena, si falla devuelve false
if ($body === false) {
    $body = null;
}
$content_type = null;
if (isset($_SERVER['CONTENT_TYPE'])) {//Si no esta vacío el content type lo almacenamos
    $content_type = $_SERVER['CONTENT_TYPE'];
}
$accept = null;
if (isset($_SERVER['HTTP_ACCEPT'])) {//Si no esta vacío el accept lo almacenamos
    $accept = $_SERVER['HTTP_ACCEPT'];
}

if (isset($_SERVER['PHP_AUTH_USER'])) {//Nos permite coger el usuario desde la cabecera Autorization Basic
    $usuario = $_SERVER['PHP_AUTH_USER'];
}
else {
    $usuario = "";
}

if (isset($_SERVER['PHP_AUTH_PW'])) {//Nos permite coger la contraseña desde la cabecera Autorization Basic
    $contrasena = $_SERVER['PHP_AUTH_PW'];
}
else {
    $contrasena = "";
}


$req = new Request($verb, $url_elements, $query_string, $body, $content_type, $accept, $usuario, $contrasena);

$token = Autentication::getBearerToken();
// route the request to the right place
$controller_name = ucfirst($url_elements[1]) . 'Controller';
/*if (class_exists($controller_name)) {//Si la clase existe
    $controller = new $controller_name();//Instanciamos el controller
    $action_name = 'manage' . ucfirst(strtolower($verb)) . 'Verb';//Conseguimos el nombre de la función a llamar
    $controller->$action_name($req);//Llamammos a la función
} //If class does not exist, we will send the request to NotFoundController
else {
    $controller = new NotFoundController();
    $controller->manage($req); //We don't care about the HTTP verb
}*/

//Si la autentificación es correcta o es un post
if(Autentication::checkAuthentication($usuario, $contrasena, $token) || (ucfirst(strtolower($verb)) == "Post" && ucfirst($url_elements[1]) == "Usuario")) {
    if (!Autentication::checkUser($req) || ucfirst(strtolower($verb)) == "Get") {//Si no existe el usuario o es un get
        if (class_exists($controller_name)) {//Si existe la clase controller
            $controller = new $controller_name();
            $action_name = 'manage' . ucfirst(strtolower($verb)) . 'Verb';
            $controller->$action_name($req);
        } //If class does not exist, we will send the request to NotFoundController
        else {
            $controller = new NotFoundController();
            $controller->manage($req); //We don't care about the HTTP verb
        }
    }
    else {
        $controller = new ConflictController(); //Lo mejor seria el 409 Conflict
        $controller->manage($req);
    }

}
else {
    $controller = new NotPermissionController();
    $controller->manage($req); //We don't care about the HTTP verb
}

//DEBUG / TESTING:
//echo "<br/>URL_ELEMENTS:" ;
//print_r ($req->getUrlElements());
//echo "<br/>VERB:" ;
//print_r ($req->getVerb());
//echo "<br/>QUERY_STRING:" ;
//print_r ($req->getQueryString());
//echo "<br/>BODY_PARAMS:" ;
//print_r ($req->getBodyParameters());

