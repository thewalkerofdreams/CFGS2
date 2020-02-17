<?php


class UsuarioModel implements JsonSerializable
{
    private $username;
    private $id;
    private $hashkey;

    /*public function __construct($id, $name, $key)
    {
        $this->id=$id;
        $this->username=$name;
        $this->hashkey=$key;
    }*/
    public function __construct(){

    }

    /**
     * Specify data which should be serialized to JSON
     * @link http://php.net/manual/en/jsonserializable.jsonserialize.php
     * @return mixed data which can be serialized by <b>json_encode</b>,
     * which is a value of any type other than a resource.
     * @since 5.4.0
     */

    //Needed if the properties of the class are private.
    //Otherwise json_encode will encode blank objects
    function jsonSerialize()
    {
        return array(
            'username' => $this->username,
            'id' => $this->id,
            'hashkey' => $this->hashkey
        );
    }

    public function __sleep(){
        return array('username' , 'id' , 'hashkey' );
    }


    /**
     * @return mixed
     */
    public function getUsername()
    {
        return $this->username;
    }

    /**
     * @param mixed $username
     */
    public function setUsername($username)
    {
        $this->username = $username;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getHashkey()
    {
        return $this->hashkey;
    }

    /**
     * @param mixed $hashkey
     */
    public function setHashkey($hashkey)
    {
        $this->hashkey = $hashkey;
    }

}