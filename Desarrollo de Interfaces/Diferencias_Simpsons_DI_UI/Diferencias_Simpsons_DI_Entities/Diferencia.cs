using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Diferencias_Simpsons_DI_Entities
{
    public class Diferencia : INotifyPropertyChanged
    {
        private int _id;
        private String _borde;

        #region Constructores
        public Diferencia()
        {
            _id = 0;
            _borde = "Transparent";
        }

        public Diferencia(int id)
        {
            this._id = id;
            _borde = "Transparent";
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

        public String Borde
        {
            get
            {
                return _borde;
            }
            set
            {
                _borde = value;
                NotifyPropertyChanged("Borde");
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
