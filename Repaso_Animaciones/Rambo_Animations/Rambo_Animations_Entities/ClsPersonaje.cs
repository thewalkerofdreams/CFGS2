using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Rambo_Animations_Entities
{
    public class ClsPersonaje : INotifyPropertyChanged
    {
        private String _srcImage;
        private ClsPunto _position;
        private int _speed;
        private ClsArma _gun;
        private char _lastMovement;

        #region Constructores
        public ClsPersonaje()
        {
            _srcImage = "/Assets/soldier.gif";
            _position = new ClsPunto();
            _speed = 10;
            _gun = new ClsArma();
            _lastMovement = ' ';
        }

        public ClsPersonaje(ClsPunto position, int speed, ClsArma gun)
        {
            _srcImage = "/Assets/soldier.gif";
            _position = position;
            _speed = speed;
            _gun = gun;
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

        public ClsArma Gun
        {
            get
            {
                return _gun;
            }
            set
            {
                _gun = value;
                NotifyPropertyChanged("Gun");
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
