using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceInvaders_Entities
{
    public class ClsPunto
    {
        private int _x;
        private int _y;

        #region Constructores
        public ClsPunto()
        {
            _x = 0;
            _y = 0;
        }

        public ClsPunto(int x, int y)
        {
            _x = x;
            _y = y;
        }
        #endregion

        #region Propiedades públicas
        public int X
        {
            get
            {
                return _x;
            }
            set
            {
                _x = value;
            }
        }

        public int Y
        {
            get
            {
                return _y;
            }
            set
            {
                _y = value;
            }
        }
        #endregion
    }
}
