using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Popups;
using Windows.UI.Xaml.Controls;

namespace Diferencias_Simpsons_DI_UI.ViewModels
{
    public class MainPageVM : INotifyPropertyChanged
    {
        private int _contadorDiferencias;
        private int evitarReplicaMensaje;

        #region Constructores
        public MainPageVM()
        {
            _contadorDiferencias = 0;
            evitarReplicaMensaje = 0;
        }
        #endregion

        #region Propiedades Públicas
        public int ContadorDiferencias
        {
            get
            {
                return _contadorDiferencias;
            }
            set
            {
                _contadorDiferencias = value;
                if (_contadorDiferencias == 8)//Si se han encontrado todas las diferencias
                {
                    if (evitarReplicaMensaje == 0)//Si el mensaje no se ha replicado por culpa del evento PropertyChanged
                    {
                        evitarReplicaMensaje++;
                        mensajeVictoria();
                    }
                }
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
        //Nos permite mostrar un mensaje de victoria por pantalla
        public async void mensajeVictoria()
        {
            await new MessageDialog("¡Has encontrado todos las diferencias!").ShowAsync();
        }
    }
}
