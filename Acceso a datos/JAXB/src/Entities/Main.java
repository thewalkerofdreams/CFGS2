package Entities;

import java.io.File;

public class Main {
    public static void main(String[] args){
        ClsManejadorPersona manejadora = new ClsManejadorPersona();

        File origen = new File("src/ArchivosXML/CorazonesSolitarios.xml");
        File origen2 = new File("src/ArchivosXML/MasCorazones.xml");
        File destino = new File("src/ArchivosXML/nuevo.xml");
        manejadora.almacenarListaPersonasJAXB(origen);
        manejadora.addPersonasJAXB(origen2);
        manejadora.ordenarLista();
        manejadora.guardarListaPersonas(destino);
    }
}