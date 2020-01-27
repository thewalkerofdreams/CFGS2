package Handlers;

import Entities.ClsPersona;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClsManejadorPersona {
    ArrayList<ClsPersona> listaPersona;
    public void abrirListaAtomosJAXB (File archivoXML){
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Atomos.class);
            Unmarshaller u = contexto.createUnmarshaller();
            listaPersona = u.unmarshal(archivoXML);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void anadirAtomo(ClsPersona nuevo){
        listaPersona.add(nuevo);
    }
    public void guardarListaAtomos(File archivoXML){
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Atomos.class);
            Marshaller marshalero = contexto.createMarshaller();
            marshalero.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter escribiente = new StringWriter();
            marshalero.marshal(listaPersona, archivoXML);
            // ahora lo marshaleamos a un stream para visualizarlo
            marshalero.marshal(listaPersona, escribiente);
            System.out.println("-----------------");
            System.out.println("Object2XML:");
            System.out.println(escribiente.toString());
            System.out.println("-----------------");
        } catch (JAXBException ex) {
            Logger.getLogger(ClsManejadorPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}