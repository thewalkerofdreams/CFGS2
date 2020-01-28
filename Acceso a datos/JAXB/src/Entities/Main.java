package Entities;

import java.io.File;

public class Main {
    public static void main(String[] args){
        ClsManejadorPersona manej = new ClsManejadorPersona();

        File origen = new File("src/ArchivosXML/CorazonesSolitarios.xml");
        File origen2 = new File("src/ArchivosXML/MasCorazones.xml");
        File destino = new File("src/ArchivosXML/nuevo.xml");
        manej.almacenarListaPersonasJAXB(origen);
        manej.addPersonasJAXB(origen2);
        //manej.ordenarLista();
        manej.guardarListaPersonas(destino);
    }
}
