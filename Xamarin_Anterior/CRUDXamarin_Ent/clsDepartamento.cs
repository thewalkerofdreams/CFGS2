using System;
using System.Collections.Generic;
using System.Text;

namespace CRUDXamarin_Ent
{
    public class clsDepartamento
    {
        public int Id { get; set; }
        public String Nombre { get; set; }

        #region Constructores
        public clsDepartamento()
        {
            Id = 0;
            Nombre = "DEFAULT";
        }

        public clsDepartamento(int id, String nombre)
        {
            Id = id;
            Nombre = nombre;
        }
        #endregion

    }
}
