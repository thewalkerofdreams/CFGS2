import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Seguro")
public class Seguro implements Serializable {

    @Id
    @Column(name="idSeguro")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idSeguro;
    @Column(name="nif")
    private String nif;
    @Column(name="nombre")
    private String nombre;
    @Column(name="apellido1")
    private String apellido1;
    @Column(name="apellido2")
    private String apellido2;
    @Column(name="edad")
    private int edad;
    @Column(name="numHijos")
    private int numHijos;
    @Column(name="fechaCreacion")
    private Date fechaCreacion;

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

    @Override
    public String toString(){
        return "Id: "+getIdSeguro() +", Nombre: "+getNombre()+", Apellido: "+getApellido1()+" "+getApellido2()+", Edad: "+getEdad()
                +", NumHijos: "+getNumHijos()+", FechaCreacion: "+getFechaCreacion().getYear() +"/"+getFechaCreacion().getMonth()+"/"+
                getFechaCreacion().getDay();
    }
}
