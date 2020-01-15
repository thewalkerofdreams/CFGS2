using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Animations_Space_DI_Entities
{
    public class ClsEstrella : INotifyPropertyChanged
    {
        private String _image;
        private int _xPoint;
        private int _yPoint;
        private double _size;

        #region Constructores
        public ClsEstrella()
        {
            this._image = "DEFAULT";
            this._xPoint = 0;
            this._yPoint = 0;
            this._size = 0.0;
        }

        public ClsEstrella(String image, int xPoint, int yPoint, double size)
        {
            this._image = image;
            this._xPoint = xPoint;
            this._yPoint = yPoint;
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

        public int XPoint
        {
            get
            {
                return _xPoint;
            }
            set
            {
                _xPoint = value;
                NotifyPropertyChanged("XPoint");
            }
        }

        public int YPoint
        {
            get
            {
                return _yPoint;
            }
            set
            {
                _yPoint = value;
                NotifyPropertyChanged("YPoint");
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
