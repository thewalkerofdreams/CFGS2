using Diferencias_Simpsons_DI_Entities;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Diferencias_Simpsons_DI_UI.ViewModels
{
    public class MainPageVM : INotifyPropertyChanged
    {
        private Diferencia _diferenciaSeleccionada;
        private Diferencia _diferencia01;
        private Diferencia _diferencia02;
        private Diferencia _diferencia03;
        private Diferencia _diferencia04;
        private Diferencia _diferencia05;
        private Diferencia _diferencia06;
        private Diferencia _diferencia07;
        private Diferencia _diferencia08;
        private int _contadorDiferencias;
        private String _mensajeVictoria;

        #region Propiedades Públicas

        public Diferencia DiferenciaSeleccionada
        {
            get
            {
                return _diferenciaSeleccionada;
            }
            set
            {
                _diferenciaSeleccionada = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia01
        {
            get
            {
                return _diferencia01;
            }
            set
            {
                _diferencia01 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia02
        {
            get
            {
                return _diferencia02;
            }
            set
            {
                _diferencia02 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia03
        {
            get
            {
                return _diferencia03;
            }
            set
            {
                _diferencia03 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia04
        {
            get
            {
                return _diferencia04;
            }
            set
            {
                _diferencia04 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia05
        {
            get
            {
                return _diferencia05;
            }
            set
            {
                _diferencia05 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia06
        {
            get
            {
                return _diferencia06;
            }
            set
            {
                _diferencia06 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia07
        {
            get
            {
                return _diferencia07;
            }
            set
            {
                _diferencia07 = value;
                NotifyPropertyChanged();
            }
        }

        public Diferencia Diferencia08
        {
            get
            {
                return _diferencia08;
            }
            set
            {
                _diferencia08 = value;
                NotifyPropertyChanged();
            }
        }

        public int ContadorDiferencias
        {
            get
            {
                return _contadorDiferencias;
            }
            set
            {
                _contadorDiferencias = value;
                NotifyPropertyChanged();
            }
        }

        public String MensajeVictoria
        {
            get
            {
                return _mensajeVictoria;
            }
            set
            {
                _mensajeVictoria = value;
                NotifyPropertyChanged();
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
