using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Diferencias_Simpsons_DI_Entities
{
    public class Diferencia
    {
        private int _id;
        private bool _descubierta;

        #region Constructores
        public Diferencia()
        {
            _id = 0;
            _descubierta = false;
        }

        public Diferencia(int id)
        {
            this._id = id;
            _descubierta = false;
        }
        #endregion

        #region Propiedades Públicas

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

        public bool Descubierta
        {
            get
            {
                return _descubierta;
            }
            set
            {
                _descubierta = value;
            }
        }

        #endregion

    }
}
