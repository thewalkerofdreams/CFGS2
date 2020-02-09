using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_ENTITIES
{
    public class clsPersona
    {
        //Fernando usa para la propiedad privada asi _nombre, pero yo usare minusculas y para los metodos
        //Mayusculas
        //private string nombre;
        //private string primerApellido;
        //private string segundoApellido;
        //private DateTime fechaNacimiento;

        public clsPersona()
        {
            IdPersona = 0;
            NombrePersona = "";
            ApellidosPersona = "";
            IDDepartamento = 0;
            FechaNacimientoPersona = new DateTime();
            TelefonoPersona = "";
            FotoPersona = new byte[1];
            //Direccion = "";
        }

        public clsPersona(int id, string nombre, string apellidos, DateTime fecha, int idDepartamento, Byte[] foto, string telefono)
        {
            IdPersona = id;
            NombrePersona = nombre;
            ApellidosPersona = apellidos;
            FechaNacimientoPersona = fecha;
            IDDepartamento = idDepartamento;
            FotoPersona = foto;
            TelefonoPersona = telefono;
            //Direccion = direccion;
        }

      
        public string NombrePersona { get; set; }
        public string ApellidosPersona { get; set; }
        public DateTime FechaNacimientoPersona { get; set; }
        public int IDDepartamento { get; set; }
        public Byte[] FotoPersona { get; set; }
        public String TelefonoPersona { get; set; }
        public int IdPersona { get; set; }
        //public string Direccion { get; set; }
    }
}
