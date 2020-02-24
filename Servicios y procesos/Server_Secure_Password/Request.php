<?php


class Request
{
    private $url_elements;
    private $query_string;
    private $verb;
    private $body_parameters;
    //in $format we store the format of the content received, but we do not use this variable
    private $format;
    //in $accept we store the format of the content that the server will send
    private $accept;
    private $token;

    public function __construct($verb, $url_elements, $query_string, $body, $content_type, $accept, $token)
    {
        $this->verb = $verb;
        $this->url_elements = $url_elements;
        $this->query_string = $query_string;
        $this->parseBody($body, $content_type);
        $this->token = $token;

        switch ($accept) {
            case 'application/json':
            case '*/*':
            case null:
                $this->accept = 'json';
                break;
            case 'application/xml':
            case 'text/xml':
                $this->accept = 'xml';
                break;
            default:
                $this->accept = 'unsupported';
                break;
        }


        return true;
    }

    private function parseBody($body, $content_type)
    {
        $parameters = array();

        switch ($this->getContentType($content_type)) {
            case 1:
                $this->format = "json";
                $parameters = json_decode($body);
                /*$body_params = json_decode($body);
                if ($body_params) {
                    foreach ($body_params as $param_name => $param_value) {
                        $parameters[$param_name] = $param_value;
                    }
                }*/
                break;

            case 2:
                $this->format = "html";
                parse_str($body, $parameters);
                /*parse_str($body, $postvars);
                foreach ($postvars as $field => $value) {
                    $parameters[$field] = $value;

                }*/

                break;
            default:
                // we could parse other supported formats here
                break;
        }
        $this->body_parameters = $parameters;

    }

    /*Devuelve
        1 - Si el content type contiene "application/json"
        2 - Si el content type contiene  "application/x-www-form-urlencoded"
        0 - Si no es ninguna de las anteriores
    Se le pasa como parametro de entrada una cadena que sera el content type a analizar
    */
    private function getContentType($contentType){
        $tipo = 0;
        /*
         * Docu sobre strstr https://www.php.net/manual/en/function.strstr.php
         * */

        if(strstr($contentType, "application/json") != false){
            $tipo = 1;
        }else if(strstr($contentType, "application/x-www-form-urlencoded") != false){
            $tipo = 2;
        }

        return $tipo;
    }

    /**
     * @return mixed
     */
    public function getUrlElements()
    {
        return $this->url_elements;
    }

    /**
     * @param mixed $url_elements
     */
    public function setUrlElements($url_elements)
    {
        $this->url_elements = $url_elements;
    }

    /**
     * @return mixed
     */
    public function getQueryString()
    {
        return $this->query_string;
    }

    /**
     * @param mixed $query_string
     */
    public function setQueryString($query_string)
    {
        $this->query_string = $query_string;
    }

    /**
     * @return mixed
     */
    public function getVerb()
    {
        return $this->verb;
    }

    /**
     * @param mixed $verb
     */
    public function setVerb($verb)
    {
        $this->verb = $verb;
    }

    /**
     * @return mixed
     */
    public function getBodyParameters()
    {
        return $this->body_parameters;
    }

    /**
     * @param mixed $body_parameters
     */
    public function setBodyParameters($body_parameters)
    {
        $this->body_parameters = $body_parameters;
    }

    /**
     * @return mixed
     */
    public function getFormat()
    {
        return $this->format;
    }

    /**
     * @param mixed $format
     */
    public function setFormat($format)
    {
        $this->format = $format;
    }

    /**
     * @return mixed
     */
    public function getAccept()
    {
        return $this->accept;
    }

    /**
     * @param mixed $accept
     */
    public function setAccept($accept)
    {
        $this->accept = $accept;
    }


    /**
     * @return mixed
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * @param mixed $llave
     */
    public function setToken($token)
    {
        $this->token = $token;
    }
}