package Entities;

import clase.Corazoncitos;
import clase.TipoPersona;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClsManejadorPersona {

    Corazoncitos listaPersonas;

    public void almacenarListaPersonasJAXB(File archivoXML){
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Corazoncitos.class);
            Unmarshaller u = contexto.createUnmarshaller();
            listaPersonas = (Corazoncitos) u.unmarshal(archivoXML);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addPersonasJAXB(File archivoXML) {
        JAXBContext contexto;
        Corazoncitos adicionales;
        try {
            contexto = JAXBContext.newInstance(Corazoncitos.class);
            Unmarshaller u = contexto.createUnmarshaller();
            adicionales = (Corazoncitos) u.unmarshal(archivoXML);

            for(TipoPersona persona:adicionales.getPersona()){
                listaPersonas.getPersona().add(persona);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ordenarLista() {
        TipoPersona[] personas = new TipoPersona[listaPersonas.getPersona().size()];
        personas = listaPersonas.getPersona().toArray(personas);
        Arrays.sort(personas);

        listaPersonas.getPersona().clear();
        for(TipoPersona persona:personas){
            listaPersonas.getPersona().add(persona);
        }
    }

    public void guardarListaPersonas(File newFile){
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Corazoncitos.class);
            Marshaller marshalero = contexto.createMarshaller();
            marshalero.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter escribiente = new StringWriter();
            marshalero.marshal(listaPersonas, newFile);
            // ahora lo marshaleamos a un stream para visualizarlo
            marshalero.marshal(listaPersonas, escribiente);
            System.out.println("-----------------");
            System.out.println("Object2XML:");
            System.out.println(escribiente.toString());
            System.out.println("-----------------");
        } catch (JAXBException ex) {
            Logger.getLogger(ClsManejadorPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}