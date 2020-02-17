<?php

require_once "ConsUsuariosModel.php";

class Autentication
{

    static function hashPassword($password) {
        return password_hash($password);
    }

    static function checkAuthentication($user, $password) {
        $pasa = false;
        $pwd = "";
        $db = DatabaseModel::getInstance();
        $db_connection = $db -> getConnection();

        $query = "SELECT " . \ConstantesDB\ConsUsuariosModel::HASHKEY . " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME . " WHERE " . \ConstantesDB\ConsUsuariosModel::USERNAME . " = ? ";
        $prep_query = $db_connection->prepare($query);
        $prep_query->bind_param('s', $user);
        $prep_query->execute();
        $prep_query->bind_result($pwd);
        while ($prep_query->fetch()) {
            if (self::verifyPassword($password, $pwd)) {
                $pasa = true;
            }
        }

        return $pasa;
    }

    static function verifyPassword($password, $hashPWD) {
        return password_verify($password, $hashPWD);
    }

    static function checkUser($request) {
        $usuarioExistente = false;
        $nombre = "";
        $name = "";
        $db = DatabaseModel::getInstance();
        $db_connection = $db -> getConnection();
        $body = null;

        $body = $request->getBodyParameters();
        $nombre = $body->name;

        $query = "SELECT " . \ConstantesDB\ConsUsuariosModel::USERNAME. " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME . " WHERE " . \ConstantesDB\ConsUsuariosModel::USERNAME . " = ? ";
        $prep_query = $db_connection -> prepare($query);
        $prep_query -> bind_param('s', $nombre);
        $prep_query -> execute();
        $prep_query -> bind_result( $name);
        while($prep_query -> fetch()) {
            if($nombre == $name) {
                $usuarioExistente = true;
            }
        }

        return $usuarioExistente;
    }
}