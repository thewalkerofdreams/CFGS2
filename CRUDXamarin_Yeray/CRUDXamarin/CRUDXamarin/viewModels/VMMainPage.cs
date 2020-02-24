using CRUDXamarin_BL.Handler;
using CRUDXamarin_BL.List;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;
using Xamarin.Forms;

namespace CRUDXamarin.viewModels
{
    public class VMMainPage : clsVMBase
    {
        private ObservableCollection<clsPersona> listadoPersonas;
        private clsPersona _personaSeleccionada;
        private INavigation _navigation;//Lo utilizaremos para navegar a las demás pantallas

        #region Constructores
        public VMMainPage()
        {
            try
            {
                cargarListados();   
                NotifyPropertyChanged("ListadoPersonas");
            }
            catch (Exception)
            {
                falloConexion();
            }
            this.DeleteCommand = new DelegateCommand(ExecuteDeleteCommand, CanExecuteDeleteCommand);
            this.AddCommand = new DelegateCommand(ExecuteAddCommand);
            this.EditCommand = new DelegateCommand(ExecuteEditCommand, CanExecuteDeleteCommand);
            this.DetailsCommand = new DelegateCommand(ExecuteDetailsCommand, CanExecuteDeleteCommand);
            _navigation = null;
        }

        public VMMainPage(INavigation navigation)
        {
            try
            {
                cargarListados();
                NotifyPropertyChanged("ListadoPersonas");
            }
            catch (Exception)
            {
                falloConexion();
            }
            this.DeleteCommand = new DelegateCommand(ExecuteDeleteCommand, CanExecuteDeleteCommand);
            this.AddCommand = new DelegateCommand(ExecuteAddCommand);
            this.EditCommand = new DelegateCommand(ExecuteEditCommand, CanExecuteDeleteCommand);
            this.DetailsCommand = new DelegateCommand(ExecuteDetailsCommand, CanExecuteDeleteCommand);
            _navigation = navigation;
        }
        #endregion

        #region Propiedades Públicas
        public clsPersona PersonaSeleccionada
        {
            get
            {            
                return this._personaSeleccionada;
            }
            set
            {
                if (_personaSeleccionada != value)//Solo se necesita si se usa xBind
                {
                    this._personaSeleccionada = value;
                    DeleteCommand.RaiseCanExecuteChanged();
                    AddCommand.RaiseCanExecuteChanged();
                    EditCommand.RaiseCanExecuteChanged();
                    DetailsCommand.RaiseCanExecuteChanged();
                    NotifyPropertyChanged("PersonaSeleccionada");
                }
            }
        }

        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get
            {
                return this.listadoPersonas;
            }
            set { this.listadoPersonas = value; }
        }
        public DelegateCommand DeleteCommand{get;}
        public DelegateCommand AddCommand{get;}
        public DelegateCommand EditCommand { get; }
        public DelegateCommand DetailsCommand { get; }

        public INavigation Navigation
        {
            get
            {
                return _navigation;
            }
            set
            {
                _navigation = value;
                NotifyPropertyChanged("Navigation");
            }
        }
        #endregion


        #region Commands
        /// <summary>
        /// Comentario: Este método nos permite eliminar a una persona de la base de datos.
        /// </summary>
        private async void ExecuteDeleteCommand()
        {
            var answer = await Application.Current.MainPage.DisplayAlert("Delete", "Do you want to delete this element?", "Yes", "No");
         
            if (answer)
            {
                clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
                try
                {
                    if (await gestionPersonasBl.eliminarPersona(this._personaSeleccionada) == 1)
                    {
                        cargarListados();
                        this._personaSeleccionada = null;
                        NotifyPropertyChanged("PersonaSeleccionada");
                    }
                    
                }
                catch (Exception)
                {
                    falloConexion();
                }
            }
        }
        /// <summary>
        /// Comentario: Este método nos permite verificar si podemos ejecutar un comando específico.
        /// </summary>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si se puede ejecutar el comando
        /// y false en caso contrario.</returns>
        private bool CanExecuteDeleteCommand()
        {
            bool habilitado = true;
            if (this._personaSeleccionada == null)
            {
                habilitado = false;
            }
            return habilitado;
        }

        /// <summary>
        /// Comentario: Este método nos permite cargar una pantalla de inserción de personas.
        /// </summary>
        private async void ExecuteAddCommand()
        {
            clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
            await _navigation.PushAsync(new CRUDXamarin.Views.AddPerson());
        }

        /// <summary>
        /// Comentario: Este método nos permite cargar una pantalla de edición de persona.
        /// </summary>
        private async void ExecuteEditCommand()
        {
            clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
            await Navigation.PushAsync(new CRUDXamarin.Views.EditPerson(_personaSeleccionada));
        }

        /// <summary>
        /// Comentario: Este método nos permite cargar una pantalla de detalles de persona.
        /// </summary>
        private async void ExecuteDetailsCommand()
        {
            clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
            await Navigation.PushAsync(new CRUDXamarin.Views.DetailsPerson(_personaSeleccionada));
        }
        #endregion

        #region Funciones Listado
        /// <summary>
        /// Comentario: Este método nos permite cargar el listado de personas.
        /// </summary>
        public async void cargarListados()
        {
            try
            {
                this.listadoPersonas = new ObservableCollection<clsPersona>(await new clsListadosPersonaBL().listadoPersonasCompleto());
                NotifyPropertyChanged("ListadoPersonas");  
            }
            catch (Exception)
            {
                falloConexion();
            }
        }
        #endregion

        #region Mensajes
        /// <summary>
        /// Comentario: Este método nos permite mostrar un mensaje de error de conexión por pantalla.
        /// </summary>
        private async void falloConexion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "Connection Error",
                   "OK"
                   );
            return;
        }
        #endregion
    }
}
