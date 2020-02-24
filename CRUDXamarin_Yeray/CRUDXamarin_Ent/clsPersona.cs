using System;
using System.Collections.Generic;


namespace CRUDXamarin_Ent
{

    public class clsPersona
    {
       
        public int idPersona { get; set; }
 
        public string nombrePersona { get; set; }
   
        public string apellidosPersona { get; set; }
 
        public DateTime fechaNacimientoPersona { get; set; }

        public string telefonoPersona { get; set; }

        public Byte[] fotoPersona { get; set; }
     
        public int idDepartamento { get; set; }

    }
    
}