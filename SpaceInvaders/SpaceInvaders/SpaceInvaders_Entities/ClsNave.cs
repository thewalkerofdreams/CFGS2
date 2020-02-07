using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace SpaceInvaders_Entities
{
    public class ClsNave : INotifyPropertyChanged
    {
        private String _srcImage;
        private ClsPunto _position;
        private int _speed;
        private char _lastMovement;

        #region Constructores
        public ClsNave()
        {
            _srcImage = "/Assets/ship.gif";
            _position = new ClsPunto(300, 397);
            _speed = 10;
            _lastMovement = ' ';
        }

        public ClsNave(ClsPunto position, int speed)
        {
            _srcImage = "/Assets/ship.gif";
            _position = position;
            _speed = speed;
            _lastMovement = ' ';
        }
        #endregion

        #region Propiedades Públicas
        public String SrcImage
        {
            get
            {
                return _srcImage;
            }
            set
            {
                _srcImage = value;
                NotifyPropertyChanged("SrcImage");
            }
        }

        public ClsPunto Position
        {
            get
            {
                return _position;
            }
            set
            {
                _position = value;
                NotifyPropertyChanged("Position");
            }
        }

        public int Speed
        {
            get
            {
                return _speed;
            }
            set
            {
                _speed = value;
                NotifyPropertyChanged("Speed");
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
        // Create the OnPropertyChanged method to raise the event
        private void NotifyPropertyChanged([CallerMemberName] String propertyName = "")
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}
