using _31_PracticaListasAnimadas_BL.Handlers;
using _31_PracticaListasAnimadas_BL.Lists;
using _31_PracticaListasAnimadas_ENTITIES;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;

namespace _31_PracticaListasAnimadas_UI.ViewModels
{
    public class MainPageVM : INotifyPropertyChanged
    {
        #region Propiedades privadas
        private clsPersona personaSeleccionada;
        private ObservableCollection<clsPersona> listadoPersonas;
        private ObservableCollection<clsDepartamento> listadoDepartamentos;
        private clsListadosPersonasBL apiPersonas = new clsListadosPersonasBL();
        private clsListadosDepartamentosBL apiDepartamentos = new clsListadosDepartamentosBL();
        private clsManejadoraBL handlerPersona = new clsManejadoraBL();
        private DelegateCommand eliminarComando;
        private DelegateCommand anhadirComando;
        private DelegateCommand guardarComando;
        #endregion

        #region PropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void NotifyPropertyChanged(string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion

        #region Constructor
        public MainPageVM()
        {
            cargarListadoPersonas();
            cargarListadoDepartamentos();
            eliminarComando = new DelegateCommand(Eliminar, () => personaSeleccionada != null);
            guardarComando = new DelegateCommand(Guardar);
            anhadirComando = new DelegateCommand(Anhadir);

        }

        #endregion

        #region Propiedades publicas
        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get
            {
                return this.listadoPersonas;
            }
            set
            {
                this.listadoPersonas = value;
                NotifyPropertyChanged("ListadoPersonas");
            }
        }

        public ObservableCollection<clsDepartamento> ListadoDepartamentos
        {
            get
            {
                return this.listadoDepartamentos;
            }
            set
            {
                this.listadoDepartamentos = value;
                NotifyPropertyChanged("ListadoDepartamentos");
            }
        }

        public clsPersona PersonaSeleccionada
        {
            get
            {
                return this.personaSeleccionada;
            }
            set
            {
                this.personaSeleccionada = value;
                eliminarComando.RaiseCanExecuteChanged();
                eliminarComando.CanExecute(personaSeleccionada);
                NotifyPropertyChanged("PersonaSeleccionada");
            }
        }

        public DelegateCommand EliminarComando
        {
            get
            {
                return eliminarComando;
            }
        } 
        public DelegateCommand AnhadirComando
        {
            get
            {
                return anhadirComando;
            }
        }
        public DelegateCommand GuardarComando
        {
            get
            {
                return guardarComando;
            }
        }
        #endregion



        #region Metodos
        private async void cargarListadoPersonas()
        {
            Task<List<clsPersona>> l = apiPersonas.listadoPersonas();
            List<clsPersona> list = await l;
            this.listadoPersonas = new ObservableCollection<clsPersona>(list);
            NotifyPropertyChanged("ListadoPersonas");
        }

        private async void cargarListadoDepartamentos()
        {
            Task<List<clsDepartamento>> l = apiDepartamentos.listadoDepartamentos();
            List<clsDepartamento> list = await l;
            this.listadoDepartamentos = new ObservableCollection<clsDepartamento>(list);
            NotifyPropertyChanged("ListadoDepartamentos");
        }
        #endregion

        #region Metodos Command
        public async void Eliminar()
        {
            ContentDialog subscribeDialog = new ContentDialog
            {
                Title = "¿Estas seguro que quiere eliminar?",
                PrimaryButtonText = "Aceptar",
                SecondaryButtonText = "Cancelar",
                DefaultButton = ContentDialogButton.Secondary
            };

            ContentDialogResult result = await subscribeDialog.ShowAsync();
            if (result == ContentDialogResult.Primary)
            {
                //No estoy seguro, pero o llamamos de nuevo a la BBDD o se lo borramos a los dos
                //listadoPersonaFiltrada.Remove(personaSeleccionada);
                await handlerPersona.borrarPersona(personaSeleccionada.IdPersona);
                cargarListadoPersonas();
                //NotifyPropertyChanged("ListadoPersonaCompleta");
                //listadoPersonaFiltrada = new ObservableCollection<clsPersona>(bbdd.listadoPersonas());
                //NotifyPropertyChanged("ListadoPersonaFiltrada");
            }
        }

        public void Anhadir()
        {
            clsPersona persona = new clsPersona();
            this.personaSeleccionada = persona;
            NotifyPropertyChanged("PersonaSeleccionada");
        }

        private async void Guardar()
        {
            await handlerPersona.anhadirPersona(personaSeleccionada);
            cargarListadoPersonas();
        }
        #endregion
    }
}
