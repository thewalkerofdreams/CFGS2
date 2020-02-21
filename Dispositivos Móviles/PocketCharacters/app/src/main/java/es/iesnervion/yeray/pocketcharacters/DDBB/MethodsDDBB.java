package es.iesnervion.yeray.pocketcharacters.DDBB;

import android.content.Context;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class MethodsDDBB {
    /**
    * Interfaz
    * Nombre: existGameMode
    * Comentario: Este método nos permite verificar si existe un GameMode en la base
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

    /**
     * Interfaz
     * Nombre: existTypeObject
     * Comentario: Este método nos permite verificar si existe un tipo de objeto en la base
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

    /**
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
     * si ya existen tipos de objeto en la base de datos o false en caso contrario.
     * */
    public boolean existAnyObjectType(Context context){
        boolean exist = false;
        ArrayList<ClsObjectType> types = new ArrayList<ClsObjectType>(AppDataBase.getDataBase(context).objectTypeDao().getAllObjectTypes());
        if (types.size() > 0){
            exist = true;
        }

        return exist;
    }

    /**
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
     * si ya existe algún GameMode en la base de datos o false en caso contrario.
     * */
    public boolean existAnyGameMode(Context context){
        boolean exist = false;
        ArrayList<ClsGameMode> gameModes = new ArrayList<ClsGameMode>(AppDataBase.getDataBase(context).gameModeDao().getAllGameModes());
        if (gameModes.size() > 0){
            exist = true;
        }

        return exist;
    }

    /**
     * Interfaz
     * Nombre: existAnyObject
     * Comentario: Este método nos permite verificar si existe algún objeto relacionado con un gameMode
     * en específico.
     * Cabecera: public boolean existAnyObject(Context context, String gameModeName)
     * Entrada:
     *   -Context context
     *   -String gameModeName
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe por lo menos un objeto para ese gamemode en la base de datos o false
     * en caso contrario.
     * */
    public boolean existAnyObject(Context context, String gameModeName){
        boolean exist = false;
        ArrayList<ClsObject> list = new ArrayList<>(AppDataBase.getDataBase(context).objectDao().getObjectsByGameMode(gameModeName));
        if (list.size() > 0){
            exist = true;
        }

        return exist;
    }

    /**
     * Interfaz
     * Nombre: existAnyObjectByType
     * Comentario: Este método nos permite verificar si existe algún objeto de un tipo
     * específico en la base de datos.
     * Cabecera: public boolean existAnyObjectByType(Context context, String objectType)
     * Entrada:
     *   -Context context
     *   -String objectType
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true
     * si ya existe por lo menos un objeto de ese tipo en la base de datos o false
     * en caso contrario.
     * */
    public boolean existAnyObjectByType(Context context, String objectType){
        boolean exist = false;
        ArrayList<ClsObject> list = new ArrayList<>(AppDataBase.getDataBase(context).objectDao().getObjectsByType(objectType));
        if (list.size() > 0){
            exist = true;
        }

        return exist;
    }

    /**
     * Interfaz
     * Nombre: existObject
     * Comentario: Este método nos permite verificar si existe un objeto en la base
     * de datos con un nombre y gamemode específico.
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

    /**
    * Interfaz
    * Nombre: statAsigned
    * Comentario: Este método nos permite comprobar si un stat específico tiene está asignado a algún
    * personaje en la base de datos.
    * Cabecera: public boolean statAsigned(Context context, int idStat)
    * Entrada:
    *   -Context context
    *   -int idStat
    * Salida:
    *   -boolean asigned
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el stat está
    * asignado a almenos un personaje o false en caso contrario.
    * */
    public boolean statAsigned(Context context, int idStat){
        boolean asigned = false;
        ArrayList<ClsCharacterAndStat> listado = new ArrayList<>(AppDataBase.getDataBase(context).characterAndStatDao().getCharacterAndStat(idStat));
        if(listado.size() > 0){
            asigned = true;
        }

        return asigned;
    }

    /*
     * Interfaz
     * Nombre: objectEquipToACharacter
     * Comentario: Este método nos permite comprobar si un objeto específico tiene está asignado a algún
     * personaje en la base de datos.
     * Cabecera: public boolean objectEquipToACharacter(Context context, int idObject)
     * Entrada:
     *   -Context context
     *   -int idObject
     * Salida:
     *   -boolean asigned
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el objeto está
     * asignado a almenos un personaje o false en caso contrario.
     * */
    public boolean objectEquipToACharacter(Context context, int idObject){
        boolean asigned = false;
        ArrayList<ClsObjectAndCharacter> listado = new ArrayList<ClsObjectAndCharacter>(AppDataBase.getDataBase(context).objectAndCharacterDao().getObjectAndCharacter(idObject));
        if(listado.size() > 0){
            asigned = true;
        }

        return asigned;
    }

    /**
     * Interfaz
     * Nombre: existStat
     * Comentario: Este método nos permite verificar si existe un stat en la base
     * de datos con un nombre y gamemode específico.
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

    /**
     * Interfaz
     * Nombre: existCharacter
     * Comentario: Este método nos permite verificar si existe un personaje en la base
     * de datos con un nombre y gamemode específico.
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

    /**
    * Interfaz
    * Nombre: existStatWithValueByCharacter
    * Comentario: Este método nos permite verificar si un personaje ya tiene un stat específico.
    * Cabecera: public boolean existStatWithValueByCharacter(Context context, ClsCharacter character, ClsStat stat)
    * Entrada:
    *   -Context context
    *   -ClsCharacter character
    *   -ClsStat stat
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

    /**
     * Interfaz
     * Nombre: existObjectWithCharacterAndObject
     * Comentario: Este método nos permite verificar si un personaje ya tiene un objeto específico.
     * Cabecera: public boolean existObjectWithCharacterAndObject(Context context, ClsCharacter character, ClsObject object)
     * Entrada:
     *   -Context context
     *   -ClsCharacter character
     *   -ClsObject object
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si ya esxiste ese objeto
     * para ese personaje específico.
     * */
    public boolean existObjectWithCharacterAndObject(Context context, ClsCharacter character, ClsObject object){
        boolean exist = false;
        ClsObjectAndCharacter clsObjectAndCharacter = AppDataBase.getDataBase(context).objectAndCharacterDao().getObjectAndCharacter(character.get_id(),
                object.get_id());
        if(clsObjectAndCharacter != null){
            exist = true;
        }

        return exist;
    }

    /**
    * Interfaz
    * Nombre: existStatsWithoutAsignToCharacter
    * Comentario: Este método nos permite verificar si existen stats de un GameMode sin asignar a un
    * personaje específico de ese GameMode.
    * Cabecera: public boolean existStatsWithoutAsignToCharacter(ClsCharacter character)
    * Entrada:
    *   -ClsCharacter character
    *   -Context context
    * Salida:
    *   -boolean exist
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el personaje
    * aún le quedan stats sin asignar de ese GameMode o false en caso contrario.
    * */
    public boolean existStatsWithoutAsignToCharacter(Context context, ClsCharacter character){
        boolean exist = false;
        ArrayList<ClsStat> stats = new ArrayList<>(AppDataBase.getDataBase(context).statDao().getStatsByGameModeAndWithoutCharacterId(character.get_gameMode(), character.get_id()));
        if(stats.size() > 0){
            exist = true;
        }

        return exist;
    }

    /**
    * Interfaz
    * Nombre: gameModeDependency
    * Comentario: Este método nos permite verificar si en la base de datos de la aplicación existe algún objeto
    * que dependa de un GameMode específico.
    * Cabecera: public boolean gameModeDependency(Context context, String gameMode)
    * Entrada:
    *   -Context context
    *   -String gameMode
    * Salida:
    *   -boolean exist
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si existe alguna
    * dependencia para ese GameMode o false en caso contrario.
    * */
    public boolean gameModeDependency(Context context, String gameMode){
        boolean exist = true;
        if((AppDataBase.getDataBase(context).objectDao().getObjectsByGameMode(gameMode)).size() == 0 &&
                (AppDataBase.getDataBase(context).statDao().getStatsByGameMode(gameMode)).size() == 0 &&
                (AppDataBase.getDataBase(context).characterDao().getCharactersByGameMode(gameMode)).size() == 0){
            exist = false;
        }

        return exist;
    }

    /*
     * Interfaz
     * Nombre: characterDependency
     * Comentario: Este método nos permite verificar si en la base de datos de la aplicación existe algún objeto
     * que dependa de un personaje específico.
     * Cabecera: public boolean characterDependency(Context context, int characterId)
     * Entrada:
     *   -Context context
     *   -int characterId
     * Salida:
     *   -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si existe alguna
     * dependencia para ese personaje o false en caso contrario.
     * */
    public boolean characterDependency(Context context, int characterId){
        boolean exist = true;
        if((AppDataBase.getDataBase(context).characterAndStatDao().getStatsAndValueByCharacter(characterId)).size() == 0 &&
                (AppDataBase.getDataBase(context).objectAndCharacterDao().getObjectAndQuantityByCharacter(characterId)).size() == 0){
            exist = false;
        }

        return exist;
    }
}
