using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RepasoApi_Entities
{
    public class ClsDepartamento
    {
        private int _id;
        private String _nombre;

        public ClsDepartamento()
        {
            _id = 0;
            _nombre = "DEFAULT";
        }

        public ClsDepartamento(int id, String nombre)
        {
            _id = id;
            _nombre = nombre;
        }

        public int Id
        {
            get
            {
                return _id;
            }
            set
            {
                _id = value;
            }
        }

        public String Nombre
        {
            get
            {
                return _nombre;
            }
            set
            {
                _nombre = value;
            }
        }

    }
}
