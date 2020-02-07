using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceInvaders_Entities
{
    public class ClsAsteroide : INotifyPropertyChanged
    {
        private String _image;
        private ClsPunto _position;
        private double _size;

        #region Constructores
        public ClsAsteroide()
        {
            this._image = "DEFAULT";
            this._position = new ClsPunto();
            this._size = 0.0;
        }

        public ClsAsteroide(String image, ClsPunto position, double size)
        {
            this._image = image;
            this._position = position;
            this._size = size;
        }
        #endregion

        #region Propiedades Públicas
        public String Image
        {
            get
            {
                return _image;
            }
            set
            {
                _image = value;
                NotifyPropertyChanged("Image");
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

        public double Size
        {
            get
            {
                return _size;
            }
            set
            {
                _size = value;
                NotifyPropertyChanged("Size");
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
