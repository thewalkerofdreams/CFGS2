package es.iesnervion.yeray.pocketcharacters.DDBB;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class MethodsDDBB {
    private boolean exist = false;

    /*
    * Interfaz
    * Nombre: existGameMode
    * Comentario: Este método nos permite si existe un GameMode en la base
    * de datos con un nombre en específico.
    * Cabecera: public boolean existGameMode(Context context, String gameModeName)
    * Entrada:
    *   -Context context
    *   -String gameModeName
    * Salida:
    *   -boolean exist
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
    * si ya existe un GameMode con ese nombre en la base de datos o false en caso contrario.
    * */
    public boolean existGameMode(Context context, String gameModeName){
        boolean exist = false;
        ClsGameMode gameMode = AppDataBase.getDataBase(context).gameModeDao().getGameMode(gameModeName);

        if (gameMode != null){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existTypeObject
     * Comentario: Este método nos permite si existe un tipo de objeto en la base
     * de datos con un nombre en específico.
     * Cabecera: public boolean existTypeObject(Context context, String typeObjectname)
     * Entrada:
     *   -Context context
     *   -String typeObjectname
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe un tipo con ese nombre en la base de datos o false en caso contrario.
     * */
    public boolean existTypeObject(Context context, String typeObjectname){
        boolean exist = false;
        ClsObjectType objectType = AppDataBase.getDataBase(context).objectTypeDao().getObjectType(typeObjectname);

        if (objectType != null){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existAnyObjectType
     * Comentario: Este método nos permite verificar si existe algun tipo de objeto en la base de
     * datos.
     * Cabecera: public boolean existAnyObjectType(Context context)
     * Entrada:
     *   -Context context
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si existen tipos de objeto en la base de datos o false en caso contrario.
     * */
    public boolean existAnyObjectType(Context context){
        boolean exist = false;
        ArrayList<ClsObjectType> types = new ArrayList<ClsObjectType>(AppDataBase.getDataBase(context).objectTypeDao().getAllObjectTypes());

        if (types.size() > 0){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existAnyGameMode
     * Comentario: Este método nos permite verificar si existe algun tipo GameMode en la base de
     * datos.
     * Cabecera: public boolean existAnyGameMode(Context context)
     * Entrada:
     *   -Context context
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si existe algún GameMode en la base de datos o false en caso contrario.
     * */
    public boolean existAnyGameMode(Context context){
        boolean exist = false;
        ArrayList<ClsGameMode> gameModes = new ArrayList<ClsGameMode>(AppDataBase.getDataBase(context).gameModeDao().getAllGameModes());

        if (gameModes.size() > 0){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existObject
     * Comentario: Este método nos permite si existe un objeto en la base
     * de datos con un nombre y gamemode en específico.
     * Cabecera: public boolean existObject(Context context, String gameModeName, String objectName)
     * Entrada:
     *   -Context context
     *   -String gameModeName
     *   -String objectName
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe un objeto con ese nombre y gamemode en la base de datos o false en caso contrario.
     * */
    public boolean existObject(Context context, String gameModeName, String objectName){
        boolean exist = false;
        ClsObject object = AppDataBase.getDataBase(context).objectDao().getObjectByGameModeAndName(gameModeName, objectName);

        if (object != null){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existStat
     * Comentario: Este método nos permite verificar si existe un stat en la base
     * de datos con un nombre y gamemode en específico.
     * Cabecera: public boolean existStat(Context context, String gameModeName, String statName)
     * Entrada:
     *   -Context context
     *   -String gameModeName
     *   -String statName
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe un stat con ese nombre y gamemode en la base de datos o false en caso contrario.
     * */
    public boolean existStat(Context context, String gameModeName, String statName){
        boolean exist = false;
        ClsStat stat = AppDataBase.getDataBase(context).statDao().getStatByGameModeAndName(gameModeName, statName);

        if (stat != null){
            exist = true;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: existCharacter
     * Comentario: Este método nos permite verificar si existe un personaje en la base
     * de datos con un nombre y gamemode en específico.
     * Cabecera: public boolean existCharacter(Context context, String gameModeName, String characterName)
     * Entrada:
     *   -Context context
     *   -String gameModeName
     *   -String characterName
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe un personaje con ese nombre y gamemode en la base de datos o false en caso contrario.
     * */
    public boolean existCharacter(Context context, String gameModeName, String characterName){
        boolean exist = false;
        ClsCharacter character = AppDataBase.getDataBase(context).characterDao().getCharacter(gameModeName, characterName);

        if (character != null){
            exist = true;
        }

        return exist;
    }

    /*
    * Interfaz
    * Nombre: existStatWithValueByCharacter
    * Comentario: Este método nos permite verificar si personaje ya tiene un stat específico.
    * Cabecera: public boolean existStatWithValueByCharacter(Context context, ClsCharacter character, String statName)
    * Entrada:
    *   -Context context
    *   -ClsCharacter character
    *   -String statName
    * Salida:
    *   -boolean exist
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si ya esxiste ese stat
    * para ese personaje específico.
    * */
    public boolean existStatWithValueByCharacter(Context context, ClsCharacter character, ClsStat stat){
        boolean exist = false;

        ClsCharacterAndStat clsCharacterAndStat = AppDataBase.getDataBase(context).characterAndStatDao().getCharacterAndStat(character.get_id(),
                stat.get_id());

        if(clsCharacterAndStat != null){
            exist = true;
        }

        return exist;
    }
}
