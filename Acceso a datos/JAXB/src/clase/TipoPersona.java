
package clase;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para TipoPersona complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoPersona">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sexo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sexoBuscado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ingresos" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Preferencias">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Gusto" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Valor" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoPersona", propOrder = {
    "id",
    "nombre",
    "sexo",
    "sexoBuscado",
    "fechaNacimiento",
    "ingresos",
    "preferencias"
})
public class TipoPersona {

    @XmlElement(name = "ID")
    protected short id;
    @XmlElement(required = true)
    protected String nombre;
    @XmlElement(required = true)
    protected String sexo;
    @XmlElement(required = true)
    protected String sexoBuscado;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaNacimiento;
    protected float ingresos;
    @XmlElement(name = "Preferencias", required = true)
    protected TipoPersona.Preferencias preferencias;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public short getID() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setID(short value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad sexo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Define el valor de la propiedad sexo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexo(String value) {
        this.sexo = value;
    }

    /**
     * Obtiene el valor de la propiedad sexoBuscado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexoBuscado() {
        return sexoBuscado;
    }

    /**
     * Define el valor de la propiedad sexoBuscado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexoBuscado(String value) {
        this.sexoBuscado = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaNacimiento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Define el valor de la propiedad fechaNacimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaNacimiento(XMLGregorianCalendar value) {
        this.fechaNacimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad ingresos.
     * 
     */
    public float getIngresos() {
        return ingresos;
    }

    /**
     * Define el valor de la propiedad ingresos.
     * 
     */
    public void setIngresos(float value) {
        this.ingresos = value;
    }

    /**
     * Obtiene el valor de la propiedad preferencias.
     * 
     * @return
     *     possible object is
     *     {@link TipoPersona.Preferencias }
     *     
     */
    public TipoPersona.Preferencias getPreferencias() {
        return preferencias;
    }

    /**
     * Define el valor de la propiedad preferencias.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPersona.Preferencias }
     *     
     */
    public void setPreferencias(TipoPersona.Preferencias value) {
        this.preferencias = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Gusto" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="Valor" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "gusto"
    })
    public static class Preferencias {

        @XmlElement(name = "Gusto")
        protected List<TipoPersona.Preferencias.Gusto> gusto;

        /**
         * Gets the value of the gusto property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the gusto property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGusto().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TipoPersona.Preferencias.Gusto }
         * 
         * 
         */
        public List<TipoPersona.Preferencias.Gusto> getGusto() {
            if (gusto == null) {
                gusto = new ArrayList<TipoPersona.Preferencias.Gusto>();
            }
            return this.gusto;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="Valor" type="{http://www.w3.org/2001/XMLSchema}byte"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "descripcion",
            "valor"
        })
        public static class Gusto {

            @XmlElement(name = "Descripcion", required = true)
            protected String descripcion;
            @XmlElement(name = "Valor")
            protected byte valor;

            /**
             * Obtiene el valor de la propiedad descripcion.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescripcion() {
                return descripcion;
            }

            /**
             * Define el valor de la propiedad descripcion.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescripcion(String value) {
                this.descripcion = value;
            }

            /**
             * Obtiene el valor de la propiedad valor.
             * 
             */
            public byte getValor() {
                return valor;
            }

            /**
             * Define el valor de la propiedad valor.
             * 
             */
            public void setValor(byte value) {
                this.valor = value;
            }

        }

    }

}
