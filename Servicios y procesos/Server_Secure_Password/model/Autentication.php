<?php
use Firebase\JWT\JWT;
require_once "ConsUsuariosModel.php";
require_once "JWT/JWT.php";

class Autentication
{
    /*
     * Interfaz
     * Nombre: generateToken
     * Comentario: Este método nos permite generar un token JWT.
     * Cabecera: static function generateToken($name)
     * Entrada:
     *  -$name
     * Salida:
     *  -$jwt
     * Postcondiciones: El método devuelve un token JWT asociado al nombre.
     * */
    static function generateToken() {
        $key = "EstoEsUnaClave";
        $payload = array(//El objeto o array de objetos php JWT
            "iss" => "http://biblioteca.devel"  //El emisor del token (Nuestra API)
        );

        $jwt = JWT::encode($payload, $key);//Obtenemos el token en formato cadena
        return $jwt;
    }

    /**
     * Get header Authorization
     * */
    static function getAuthorizationHeader(){
        $headers = null;
        if (isset($_SERVER['Authorization'])) {
            $headers = trim($_SERVER["Authorization"]);
        }
        else if (isset($_SERVER['HTTP_AUTHORIZATION'])) { //Nginx or fast CGI
            $headers = trim($_SERVER["HTTP_AUTHORIZATION"]);
        } elseif (function_exists('apache_request_headers')) {
            $requestHeaders = apache_request_headers();
            // Server-side fix for bug in old Android versions (a nice side-effect of this fix means we don't care about capitalization for Authorization)
            $requestHeaders = array_combine(array_map('ucwords', array_keys($requestHeaders)), array_values($requestHeaders));
            //print_r($requestHeaders);
            if (isset($requestHeaders['Authorization'])) {
                $headers = trim($requestHeaders['Authorization']);
            }
        }
        return $headers;
    }
    /**
     * get access token from header
     * */
    static function getBearerToken() {
        $headers = self::getAuthorizationHeader();
        // HEADER: Get the access token from the header
        if (!empty($headers)) {
            if (preg_match('/Bearer\s(\S+)/', $headers, $matches)) {
                return $matches[1];
            }
        }
        return null;
    }

    /*
     * Interfaz
     * Nombre: hashPassword
     * Comentario: Este método nos permite convertir una contraseña a hashCode.
     * Cabecera: static function hashPassword($password)
     * Entrada:
     *  -$password
     * Salida:
     *  -$hashPassword
     * Postcondiciones: El método devuelve la clave en hashCode asociada al nombre.
     * */
    static function hashPassword($password) {
        return password_hash($password);
    }

    /*
     * Interfaz
     * Nombre: checkAuthentication
     * Comentario: Este método nos permite verificar si un usuario con un nombre y contraseña específico
     * existe en la base de datos.
     * Cabecera: static function checkAuthentication($userName, $password)
     * Entrada:
     *  -$userName
     *  -$password
     * Salida:
     *  -$existe
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre,
     * true si existe un usuario con ese nombre y contraseña específica y false en
     * caso contrario.
     * */
    static function checkAuthentication($userName, $password, $token = null) {
        $existe = false;
        $pwd = "";
        $db = DatabaseModel::getInstance();
        $db_connection = $db->getConnection();


        if($token == null){//Si no se pasa ningún token
            $query = "SELECT " . \ConstantesDB\ConsUsuariosModel::HASHKEY . " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME . " WHERE " . \ConstantesDB\ConsUsuariosModel::USERNAME . " = ? ";
            $prep_query = $db_connection->prepare($query);
            $prep_query->bind_param('s', $userName);
            $prep_query->execute();
            $prep_query->bind_result($pwd);
            while ($prep_query->fetch()) {
                if (self::verifyPassword($password, $pwd)) {
                    $existe = true;
                }
            }
        }else{
            $key = "EstoEsUnaClave";
            try {
                JWT::decode($token, $key, array('HS256'));//Decodificamos
                $existe = true;
            }
            catch (Exception $e) {
                echo 'Error de autentificación';
            }
        }


        return $existe;
    }

    /*
     * Interfaz
     * Nombre: verifyPassword
     * Comentario: Este método nos permite verificar si una clave es equivalente
     * a otra en hashCode.
     * Cabecera: static function verifyPassword($password, $hashPassword)
     * Entrada:
     *  -$password
     *  -$hashPassword
     * Salida:
     *  -$valide
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si las claves son equivalentes o false en caso contrario.
     * */
    static function verifyPassword($password, $hashPassword) {
        return password_verify($password, $hashPassword);
    }

    /*
     * Interfaz
     * Nombre: checkUser
     * Comentario: Este método nos permite verificar si existe un usuario en la base
     * de datos con un nombre en específico. Obtendremos el nombre a través de un
     * request.
     * Cabecera: static function checkUser($request)
     * Entrada:
     *  -$request
     * Salida:
     *  -$usuarioExistente
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si existe el usuario en la base de datos y false en caso contrario.
     * */
    static function checkUser($username) {
        $usuarioExistente = false;
        $name = "";
        $nombre = "";
        $db = DatabaseModel::getInstance();
        $db_connection = $db -> getConnection();
        $body = null;

        //$body = $request->getBodyParameters();
        //$nombre = $body->username;

        $query = "SELECT " . \ConstantesDB\ConsUsuariosModel::USERNAME. " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME . " WHERE " . \ConstantesDB\ConsUsuariosModel::USERNAME . " = ? ";
        $prep_query = $db_connection -> prepare($query);
        $prep_query -> bind_param('s', $username);
        $prep_query -> execute();
        $prep_query -> bind_result( $name);
        while($prep_query -> fetch()) {
            if($username == $name) {
                $usuarioExistente = true;
            }
        }

        return $usuarioExistente;
    }
}