package es.iesnervion.yeray.pocketcharacters.DDBB;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

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
}
