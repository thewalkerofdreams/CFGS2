using _31_PracticaListasAnimadas_BL.Lists;
using _31_PracticaListasAnimadas_ENTITIES;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_UI.ViewModels
{
    public class MainPageVM : INotifyPropertyChanged
    {
        #region Propiedades privadas
        private clsPersona personaSeleccionada;
        private ObservableCollection<clsPersona> listadoPersonas;
        private clsListadosPersonasBL api = new clsListadosPersonasBL();

        #endregion

        #region Constructor
        public MainPageVM()
        {
            cargarListado();
        }
        #endregion

        #region Propiedades publicas
        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get
            {
                return listadoPersonas;
            }
            set
            {
                listadoPersonas = value;
                NotifyPropertyChanged("ListadoPersonas");
            }
        }

        public clsPersona PersonaSeleccionada
        {
            get
            {
                return personaSeleccionada;
            }
            set
            {
                personaSeleccionada = value;
                NotifyPropertyChanged("PersonaSeleccionada");
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

        #region Metodos
        private async void cargarListado()
        {
            Task<List<clsPersona>> l = api.listadoPersonas();
            List<clsPersona> list = await l;
            this.listadoPersonas = new ObservableCollection<clsPersona>(list);
            NotifyPropertyChanged("ListadoPersonas");
        }
        #endregion
    }
}
