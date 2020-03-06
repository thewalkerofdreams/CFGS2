import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Seguro implements Serializable {

    private int idSeguro;
    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    private int numHijos;
    private Date fechaCreacion;
    private Set<AsistenciaMedica> asistenciasMedicas;

    public Seguro(){
    }

    public Seguro(String nif, String nombre, String apellido1, String apellido2, int edad, int numHijos, Date fechaCreacion){
        this.nif = nif;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.numHijos = numHijos;
        this.fechaCreacion = fechaCreacion;
    }

    public Seguro(int idSeguro, String nif, String nombre, String apellido1, String apellido2, int edad, int numHijos, Date fechaCreacion){
        this.idSeguro = idSeguro;
        this.nif = nif;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.numHijos = numHijos;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(int idSeguro) {
        this.idSeguro = idSeguro;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(int numHijos) {
        this.numHijos = numHijos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<AsistenciaMedica> getAsistenciasMedicas() {
        return asistenciasMedicas;
    }

    public void setAsistenciasMedicas(Set<AsistenciaMedica> asistenciasMedicas) {
        this.asistenciasMedicas = asistenciasMedicas;
    }

    @Override
    public String toString(){
        return "Id: "+getIdSeguro() +", Nombre: "+getNombre()+", Apellido: "+getApellido1()+" "+getApellido2()+", Edad: "+getEdad()
                +", NumHijos: "+getNumHijos()+", FechaCreacion: "+getFechaCreacion().getYear() +"/"+getFechaCreacion().getMonth()+"/"+
                getFechaCreacion().getDay();
    }
}
