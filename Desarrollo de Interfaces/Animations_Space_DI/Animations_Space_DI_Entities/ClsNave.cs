using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Animations_Space_DI_Entities
{
    public class ClsNave : INotifyPropertyChanged
    {
        private String _addressImage;
        private int _xPosition;
        private int _movimiento;
        private char _lastMovement;

        #region Constructores
        public ClsNave()
        {
            _addressImage = "DEFAULT";
            _xPosition = 0;
            _movimiento = 0;
            _lastMovement = ' ';
        }

        public ClsNave(String addressImage, int xPosition, int movimiento)
        {
            this._addressImage = addressImage;
            this._xPosition = xPosition;
            this._movimiento = movimiento;
        }
        #endregion

        #region Propiedades Públicas
        public String AddressImage
        {
            get
            {
                return _addressImage;
            }
            set
            {
                _addressImage = value;
                NotifyPropertyChanged("AddressImage");
            }
        }

        public int XPosition{
            get
            {
                return _xPosition;
            }
            set
            {
                _xPosition = value;
                NotifyPropertyChanged("XPosition");
            }
        }

        public int Movimiento
        {
            get
            {
                return _movimiento;
            }
            set
            {
                _movimiento = value;
                NotifyPropertyChanged("Movimiento");
            }
        }

        public char LastMovement
        {
            get
            {
                return _lastMovement;
            }
            set
            {
                _lastMovement = value;
                NotifyPropertyChanged("LastMovement");
            }
        }
        #endregion

        #region PropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;

        protected virtual void NotifyPropertyChanged(string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}
