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




        //Metodos añadidos
        /*public void Clone(clsPersona otraPersona)
        {
            this.nombrePersona = otraPersona.nombrePersona;
            this.apellidosPersona = otraPersona.apellidosPersona;
            this.fechaNacimientoPersona = otraPersona.fechaNacimientoPersona;
            this.idPersona = otraPersona.idPersona;
            this.fotoPersona = otraPersona.fotoPersona;
            this.idDepartamento = otraPersona.idDepartamento;
            this.telefonoPersona = otraPersona.telefonoPersona;

           
        }*/

        /*public override bool Equals(Object obj)
        {
            bool isEqual = false;
            clsPersona oPersona;
            if(obj != null && obj.GetType() == typeof(clsPersona))
            {
                oPersona = obj as clsPersona;
                if (this.idPersona == oPersona.idPersona
                    &&
                    this.nombrePersona == oPersona.nombrePersona
                    &&
                    this.apellidosPersona == oPersona.apellidosPersona
                    &&
                    this.fechaNacimientoPersona.Day == oPersona.fechaNacimientoPersona.Day
                    &&
                    this.fechaNacimientoPersona.Month == oPersona.fechaNacimientoPersona.Month
                    &&
                    this.fechaNacimientoPersona.Year == oPersona.fechaNacimientoPersona.Year
                    &&
                    this.fotoPersona == oPersona.fotoPersona
                    &&
                    this.idDepartamento == oPersona.idDepartamento
                    &&
                    this.telefonoPersona == oPersona.telefonoPersona
                    )
                {
                    isEqual = true;
                }
            }
            return isEqual;
        }*/
    }
    
}