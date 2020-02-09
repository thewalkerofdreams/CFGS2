using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rambo_Animations_Entities
{
    public class ClsArma
    {
        private double _power;//Nos indicará el tamaño de sus balas
        private int _cadence;//Nois indicará el número de balas que dispara por segundo
        private double _scope;//Nos indica el alcance del arma.
        private int _ammo;

        #region Constructores
        public ClsArma()
        {
            _power = 1;
            _cadence = 1;
            _scope = 10;
            _ammo = 5;
        }

        public ClsArma(double power, int cadence, double scope, int ammo)
        {
            _power = power;
            _cadence = cadence;
            _scope = scope;
            _ammo = ammo;
        }
        #endregion

        #region Propiedades públicas
        public double Power
        {
            get
            {
                return _power;
            }
            set
            {
                _power = value;
            }
        }

        public int Cadence
        {
            get
            {
                return _cadence;
            }
            set
            {
                _cadence = value;
            }
        }

        public double Scope
        {
            get
            {
                return _scope;
            }
            set
            {
                _scope = value;
            }
        }

        public int Ammo
        {
            get
            {
                return _ammo;
            }
            set
            {
                _ammo = value;
            }
        }
        #endregion
    }
}
