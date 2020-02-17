<?php

require_once "ConsUsuariosModel.php";


class UsuarioHandlerModel
{

    public static function getUsuario($id)
    {
        $listaUsuarios = null;

        $db = DatabaseModel::getInstance();
        $db_connection = $db->getConnection();

        $valid = self::isValid($id);

        //If the $id is valid or the client asks for the collection ($id is null)
        if ($valid === true || $id == null) {
            $query = "SELECT " . \ConstantesDB\ConsUsuariosModel::ID . ","
                . \ConstantesDB\ConsUsuariosModel::USERNAME . ","
                . \ConstantesDB\ConsUsuariosModel::HASHKEY . " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME;


            if ($id != null) {
                $query = $query . " WHERE " . \ConstantesDB\ConsUsuariosModel::ID . " = ?";
            }

            $prep_query = $db_connection->prepare($query);

            //IMPORTANT: If we do not want to expose our primary keys in the URIS,
            //we can use a function to transform them.
            //For example, we can use hash_hmac:
            //http://php.net/manual/es/function.hash-hmac.php
            //In this example we expose primary keys considering pedagogical reasons

            if ($id != null) {
                $prep_query->bind_param('s', $id);
            }

            $prep_query->execute();
            $listaUsuarios = array();

            //IMPORTANT: IN OUR SERVER, I COULD NOT USE EITHER GET_RESULT OR FETCH_OBJECT,
            // PHP VERSION WAS OK (5.4), AND MYSQLI INSTALLED.
            // PROBABLY THE PROBLEM IS THAT MYSQLND DRIVER IS NEEDED AND WAS NOT AVAILABLE IN THE SERVER:
            // http://stackoverflow.com/questions/10466530/mysqli-prepared-statement-unable-to-get-result

            $prep_query->bind_result($id, $username, $hashkey);
            while ($prep_query->fetch()) {
                $username = utf8_encode($username);
                $user = new UsuarioModel();
                $user->setId($id);
                $user->setUsername($username);
                $user->setHashkey($hashkey);
                $listaUsuarios[] = $user;
            }

//            $result = $prep_query->get_result();
//            for ($i = 0; $row = $result->fetch_object(UsuarioModel::class); $i++) {
//
//                $listaLibros[$i] = $row;
//            }
        }
        $db_connection->close();

        return $listaUsuarios;
    }

    //returns true if $id is a valid id for a book
    //In this case, it will be valid if it only contains
    //numeric characters, even if this $id does not exist in
    // the table of books
    public static function isValid($id)
    {
        $res = false;

        if (ctype_digit($id)) {
            $res = true;
        }
        return $res;
    }

    /*
     * Interfaz
     * Nombre: modUsuario
     * Comentario: Este método nos permite modificar un usuario de la base de datos.
     * Cabecera: public static function modUsuario($bodyParameters, $id).
     * Entrada:
     *  -$bodyParameters
     *  -$id
     * Salida:
     *  -$libroModificado
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre,
     * true si se ha conseguido modificar al usuario o false en caso contrario.
     * */
    public static function modUsuario($bodyParameters, $id)
    {
        $db = DatabaseModel::getInstance();
        $db_connection = $db->getConnection();
        $usuarioCreado = null;
        $usuarioModificado = false;

        $id = $id;
        $username = $bodyParameters->titulo; //esto es el titulo
        $hashkey = $bodyParameters->numpag; //esto es el número de paginas

        $query = "UPDATE ".\ConstantesDB\ConsUsuariosModel::TABLE_NAME
            ." SET ".
            \ConstantesDB\ConsUsuariosModel::USERNAME .
            " = ? ,".
            \ConstantesDB\ConsUsuariosModel::HASHKEY
            ."= ? " .
            " WHERE " .
            \ConstantesDB\ConsUsuariosModel::ID .
            " = ?"
        ;

        $prep_query = $db_connection->prepare($query);

        $prep_query->bind_param('sii', $username, $hashkey, $id);

        $prep_query->execute();

        if ($prep_query->affected_rows > 0) {
            $usuarioModificado = true;
        }

        $db_connection->close();

        return $usuarioModificado;
    }

    /*
     * Interfaz
     * Nombre: deleteUsuario
     * Comentario: Este método nos permite eliminar un usuario de la base de datos.
     * Cabecera: public static function deleteUsuario($id)
     * Entrada:
     *  -$id
     * Salida:
     *  -$filasAfectadas
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que será el número de filas afectadas.
     * */
    public static function deleteUsuario($id){
        $filasAfectadas = 0;
        $db = DatabaseModel::getInstance();
        $db_connection = $db->getConnection();

        if($id != null && self::isValid($id)){//Si cod es diferente de null y es válido
            $query = "DELETE FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME
                . " WHERE " . \ConstantesDB\ConsUsuariosModel::ID . " = ?";

            $prep_query = $db_connection->prepare($query);//Almacenamos la consulta preparada

            $prep_query->bind_param('s', $id);//Bindeamos el parametros id para la consulta

            $filasAfectadas = $prep_query->execute();//Ejecutamos la consulta, almacenamos el número de filas afectadas
        }

        return $filasAfectadas;
    }

    /*
     * Interfaz
     * Nombre: postUsuario
     * Comentario: Este método nos permite insertar un nuevo usuario en la base de datos.
     * Cabecera: public static function postUsuario($bodyParameters)
     * Entrada:
     *  -$bodyParameters
     * Salida:
     *  -$Usuario
     * Postcondiciones: El método inserta un nuevo usuario en la base de datos. Devuelve
     * ese mismo libro con la nueva id.
     * */
    public static function postUsuario($bodyParameters)
    {
        $db = DatabaseModel::getInstance();
        $db_connection = $db->getConnection();
        $usuarioCreado = null;
        $Usuario = null;

        $username = $bodyParameters->username; //en teoria esto es el titulo
        $hashkey = password_hash($bodyParameters->hashkey, PASSWORD_DEFAULT); //esto el n paginas

        $query = "INSERT INTO ".\ConstantesDB\ConsUsuariosModel::TABLE_NAME
            ."(".
            \ConstantesDB\ConsUsuariosModel::USERNAME .
            ",".
            \ConstantesDB\ConsUsuariosModel::HASHKEY
            .")".
            " VALUES(?,?)";


        $prep_query = $db_connection->prepare($query);

        $prep_query->bind_param('ss', $username, $hashkey);

        $prep_query->execute();

        //$filasAfectadas = $prep_query->affected_rows;//Para saber el número de filas afectadas
        //Ahora obtendremos la id del libro recien creado
        //Ahora obtendremos la id
        $query02 = "SELECT " . \ConstantesDB\ConsUsuariosModel::ID . " FROM " . \ConstantesDB\ConsUsuariosModel::TABLE_NAME . " WHERE " . \ConstantesDB\ConsUsuariosModel::USERNAME .
            " = '" . $username . "' AND " . \ConstantesDB\ConsUsuariosModel::HASHKEY . " = '" . $hashkey."'";
        $prep_query02 = $db_connection->prepare($query02);//Almacenamos la consulta preparada

        $prep_query02->execute();

        $prep_query02->bind_result($id);//Indicamos la variable de cod

        $prep_query02->fetch();//Marcamos la primera y única fila

        $id01 = $id;//La almacenamos

        $Usuario = new UsuarioModel();
        $Usuario->setUsername($username);
        $Usuario->setHashkey($hashkey);

        $db_connection->close();

        return $Usuario;
    }
}