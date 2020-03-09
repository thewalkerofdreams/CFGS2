import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="AsistenciaMedica")
public class AsistenciaMedica implements Serializable {

    @Id
    @Column(name="idAsistenciaMedica")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idAsistenciaMedica;

    @Column(name="breveDescripcion")
    private String breveDescripcion;

    @Column(name="lugar")
    private String lugar;

    @ManyToOne
    @JoinColumn(name="idSeguro")
    private Seguro seguro;

    public AsistenciaMedica(){

    }

    public AsistenciaMedica(Seguro seguro, String breveDescripcion, String lugar){
        this.breveDescripcion = breveDescripcion;
        this.lugar = lugar;
        this.seguro = seguro;
    }

    public AsistenciaMedica(int idAsistenciaMedica, Seguro seguro, String breveDescripcion, String lugar){
        this.idAsistenciaMedica = idAsistenciaMedica;
        this.breveDescripcion = breveDescripcion;
        this.lugar = lugar;
        this.seguro = seguro;
    }

    public int getIdAsistenciaMedica() {
        return idAsistenciaMedica;
    }

    public void setIdAsistenciaMedica(int idAsistenciaMedica) {
        this.idAsistenciaMedica = idAsistenciaMedica;
    }

    public String getBreveDescripcion() {
        return breveDescripcion;
    }

    public void setBreveDescripcion(String breveDescripcion) {
        this.breveDescripcion = breveDescripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    @Override
    public String toString(){
        return "idSeguro: "+getSeguro().getIdSeguro()+", BreveDescripcion: "+getBreveDescripcion()+", Lugar: "+getLugar();
    }
}
