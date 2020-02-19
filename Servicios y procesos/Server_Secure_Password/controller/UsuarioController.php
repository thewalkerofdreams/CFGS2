<?php

require_once "Controller.php";


class UsuarioController extends Controller
{
    public function manageGetVerb(Request $request)
    {
        $listaUsuarios = null;
        $id = null;
        $response = null;
        $code = null;

        //if the URI refers to a libro entity, instead of the usuario collection
        if (isset($request->getUrlElements()[2])) {
            $id = $request->getUrlElements()[2];
        }

        $listaUsuarios = UsuarioHandlerModel::getUsuario($id);

        if ($listaUsuarios != null) {
            $code = '200';
        } else {
            //We could send 404 in any case, but if we want more precission,
            //we can send 400 if the syntax of the entity was incorrect...
            if (UsuarioHandlerModel::isValid($id)) {
                $code = '404';
            } else {
                $code = '400';
            }
        }

        $response = new Response($code, null, $listaUsuarios, $request->getAccept());
        $response->generate();
    }

    /*
     * Interfaz
     * Nombre: manageDeleteVerb
     * Comentario: Este controlador nos permite manejar el verbo Delete.
     * Cabecera: public function manageDeleteVerb(Request $request)
     * Entrada:
     *  -Request $request
     * Postcondiciones: La función devuelve un tipo Response, es decir, una respuesta con el código
     * de respuesta y el tipo aceptado.
     * */
    public function manageDeleteVerb(Request $request)
    {
        $filasAfectadas = null;
        $id = null;
        $response = null;
        $code = null;
        $header = null;

        //if the URI refers to a libro entity, instead of the libro collection
        if (isset($request->getUrlElements()[2])) {
            $id = $request->getUrlElements()[2];
        }

        $filasAfectadas = UsuarioHandlerModel::deleteUsuario($id);//Si id es nulo eliminará todos los libros de la base de datos

        if ($filasAfectadas > 0) {//Si se ha eliminado algún libro
            $code = '200';
            $cadena = Autentication::generateToken();
            $header['Authorization'] = "Bearer " . $cadena;
        } else {
            //We could send 404 in any case, but if we want more precission,
            //we can send 400 if the syntax of the entity was incorrect...
            if (UsuarioHandlerModel::isValid($id)) {//Si el id es válido, es decir, es un número
                $code = '404';
            } else {
                $code = '400';
            }
        }

        $response = new Response($code, $header, null, $request->getAccept());
        $response->generate();
    }

    /*
     * Interfaz
     * Nombre: managePutVerb
     * Comentario: Este controlador nos permite manejar el verbo Put.
     * Cabecera: public function managePutVerb(Request $request)
     * Entrada:
     *  -Request $request
     * Postcondiciones: La función devuelve un tipo Response, es decir, una respuesta con el código
     * de respuesta y el tipo aceptado.
     * */
    public function managePutVerb(Request $request)
    {
        $usuarioModificado = false;
        $id = null;
        $response = null;
        $code = null;
        $header = null;

        //if the URI refers to a libro entity
        if (isset($request->getUrlElements()[2])) {
            $id = $request->getUrlElements()[2];
        }

        $parametros =(object)$request->getBodyParameters();
        //$libro = new UsuarioModel($id,$parametros->titulo,$parametros->numpag);

        if($parametros->username != null && $parametros->hashkey != null){
            $usuarioModificado = UsuarioHandlerModel::modUsuario($request->getBodyParameters(), $id);
        }

        if ($usuarioModificado == true) {//Si se ha conseguido modifica algún libro
            $code = '200';
            $cadena = Autentication::generateToken();
            $header['Authorization'] = "Bearer " . $cadena;
        } else {
            //We could send 404 in any case, but if we want more precission,
            //we can send 400 if the syntax of the entity was incorrect...
            if (UsuarioHandlerModel::isValid($id)) {//Si el id es válido, es decir, es un número
                $code = '404';
            } else {
                $code = '400';
            }

        }

        $response = new Response($code, $header, null, $request->getAccept());
        $response->generate();
    }

    /*
     * Interfaz
     * Nombre: managePostVerb
     * Comentario: Este controlador nos permite manejar el verbo Post.
     * Cabecera: public function managePostVerb(Request $request)
     * Entrada:
     *  -Request $request
     * Postcondiciones: La función devuelve un tipo Response, es decir, una respuesta con el código
     * de respuesta, el body con el cuerpo del nuevo usuario y el tipo aceptado.
     * */
    public function managePostVerb(Request $request)
    {
        $usuario = null;
        $id = null;
        $response = null;
        $code = null;
        $header = null;

        //if the URI refers to a libro entity, instead of the libro collection
        if (isset($request->getUrlElements()[2])) {
            $id = $request->getUrlElements()[2];
        }

        //$parametros =(Object) $request->getBodyParameters();
        //$libroEntrada = new UsuarioModel($parametros);

        $usuario = UsuarioHandlerModel::postUsuario($request->getBodyParameters());

        if ($usuario != null) {//Si se ha encontrado algún libro
            $code = '200';
            $cadena = Autentication::generateToken();//Generamos el token
            $header['Authorization'] = "Bearer " . $cadena;
        } else {
            //We could send 404 in any case, but if we want more precission,
            //we can send 400 if the syntax of the entity was incorrect...
            if (UsuarioHandlerModel::isValid($id)) {//Si el id es válido, es decir, es un número
                $code = '404';
            } else {
                $code = '400';
            }

        }

        $response = new Response($code, $header, $usuario, $request->getAccept());
        $response->generate();
    }
}