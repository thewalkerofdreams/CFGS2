<?php

include "Database.php";
include "tabla1.php";

class GestionUsuarios
{
    /*
     * Interfaz
     * Nombre: insertarUsuario
     * Comentario: Este método nos permite insertar un usuario en la base de datos.
     * Cabecera: function insertarUsuario($usuario, $pass)
     * Entrada:
     *  -$usuario
     *  -$pass
     * Precondiciones:
     *  -$usuario y $pass no deben estar vacíos.
     * Postcondiciones: El método inserta el nuevo usuario con su contraseña en la base de datos.
     * */
    function insertarUsuario($usuario, $pass){
        $conexion = Database::getInstance();
        $conection = $conexion->getConnection();//Obtenemos la conexión

        $pass = password_hash($pass, PASSWORD_BCRYPT);//Obtenemos el código hash a través de la contraseña

        $stmt = $conection->prepare("INSERT INTO ".\Constantes_DB\tabla1::TABLE_NAME.
        "(".\Constantes_DB\tabla1::HASHKEY.", ".\Constantes_DB\tabla1::NAME.") VALUES(?,?)");
        $stmt->bind_param('ss',$pass, $usuario);

        $result = $stmt->execute();//Ejecutamos la consulta

        $stmt->close();
        $conexion->closeConnection();//Cerramos la conexión

        return $result;
    }

    /*
     * Interfaz
     * Nombre: consultarUsuario
     * Comentario: Este método nos permite verificar si un usuario existe en la base de datos.
     * Cabecera: function consultarUsuario($usuario)
     * Entrada:
     *  -$usuario
     * Salida:
     *  -$ret
     * Postcondiciones: La función devuelve un valor booleano asociado al nombre, true si el usuario
     * existe en la base de datos y false en caso contrario.
     * */
    function consultarUsuario($usuario){
        $ret = false;
        $conexion = Database::getInstance();
        $conection = $conexion->getConnection();//Obtenemos la conexión

        $stmt = $conection->prepare("SELECT * FROM ".\Constantes_DB\tabla1::TABLE_NAME." WHERE ".\Constantes_DB\tabla1::NAME."=?");
        $stmt->bind_param('s', $usuario);

        $stmt->execute();//Ejecutamos la consulta

        if($stmt->num_rows > 0){//Si el número de filas es mayor que 0 significa que hay como mínimo un usuario con ese nombre en la BBDD
            $ret = true;
        }

        $stmt->close();
        $conexion->closeConnection();//Cerramos la conexión

        return $ret;
    }

    /*
     * Interfaz
     * Nombre: obtenerHash
     * Comentario: Este método nos permite obtener el hashcode de la contraseña de un usuario.
     * Cabecera: function obtenerHash($usuario)
     * Entrada:
     *  -$usuario
     * Postcondiciones: La función devuelve un hashCode de una contraseña si $usuario coincide con el nombre de algún
     * nombre de usuario de la base de datos, en caso contrario la función devuelve una cadena vacía.
     * */
    function obtenerHash($usuario){
        $conexion = Database::getInstance();
        $conection = $conexion->getConnection();

        $stmt = $conection->prepare("SELECT ".\Constantes_DB\tabla1::HASHKEY." FROM ".\Constantes_DB\tabla1::TABLE_NAME.
            " WHERE ".\Constantes_DB\tabla1::NAME."=?");
        $stmt->bind_param('s', $usuario);

        $stmt->execute();//Ejecutamos la consulta
        $result = $stmt->get_result();

        $row = $result->fetch_assoc();//Obtenemos la fila

        $stmt->close();
        $conexion->closeConnection();//Cerramos la conexión

        return $row;
    }
}