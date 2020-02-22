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
        private clsPersona _personaAux { get; set; }
        private bool _visivilityButtonsSelectedPerson;

        #region Constructores
        public VMMainPage()
        {
            try
            {
                cargarListados();   //esto esta mal
                NotifyPropertyChanged("ListadoPersonas");
            }
            catch (Exception)
            {
                falloConexion();
            }
            this._personaAux = new clsPersona();
            this.DeleteCommand = new DelegateCommand(ExecuteDeleteCommand, CanExecuteDeleteCommand);
            _visivilityButtonsSelectedPerson = false;//Nos permite habilitar los botones cuando se seleccione una persona
        }
        #endregion

        #region Propiedades Públicas
        public clsPersona PersonaAux
        {
            get
            {
                NotifyPropertyChanged("PersonaSeleccionada");
                return this._personaAux;
            }
            set
            {
                this._personaSeleccionada = value;
            }
        }

        public clsPersona PersonaSeleccionada
        {
            get
            {
                try
                {
                    if (this._personaSeleccionada != null)
                    {
                        clsListadosPersonaBL listadosPersonaBL = new clsListadosPersonaBL();
                        clsPersona personaReal = listadosPersonaBL.obtenerObjetoPersonaPorID(this._personaSeleccionada.idPersona);
                    }
                }
                catch (Exception)
                {

                }
                return this._personaSeleccionada;
            }
            set
            {
                if (_personaSeleccionada != value)//esto solo se necesita si usas xBind
                {
                    this._personaSeleccionada = value;
                    DeleteCommand.RaiseCanExecuteChanged();
                    NotifyPropertyChanged("PersonaSeleccionada");
                    _visivilityButtonsSelectedPerson = true;
                    NotifyPropertyChanged("VisivilityButtonsSelectedPerson");
                }
            }
        }

        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get
            {
                //cargarListados();
                return this.listadoPersonas;
            }
            set { this.listadoPersonas = value; }
        }
        public DelegateCommand DeleteCommand
        {
            get;

        }
        public DelegateCommand SaveCommand
        {
            get;

        }

        public bool VisivilityButtonsSelectedPerson
        {
            get
            {
                return _visivilityButtonsSelectedPerson;
            }
            set
            {
                _visivilityButtonsSelectedPerson = value;
                NotifyPropertyChanged("VisivilityButtonsSelectedPerson");
            }
        }
        #endregion


        #region Commands
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
                        _visivilityButtonsSelectedPerson = false;
                        NotifyPropertyChanged("VisivilityButtonsSelectedPerson");  
                    }
                    
                }
                catch (Exception)
                {
                    falloConexion();
                }

            }

        }
        private bool CanExecuteDeleteCommand()
        {
            bool habilitado = true;
            if (this._personaSeleccionada == null)
            {
                habilitado = false;
            }
            return habilitado;
        }

        #endregion

        #region Funciones Listado
        private async void cargarListados()
        {
            try
            {
                this.listadoPersonas = new ObservableCollection<clsPersona>
                (await new clsListadosPersonaBL().listadoPersonasOrdinario());
                
                NotifyPropertyChanged("ListadoPersonas");
                
            }
            catch (Exception)
            {
                falloConexion();
            }

        }
        #endregion

        #region Mensajes
        private async void falloConexion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "Alerta mannn",
                   "OK"
                   );
            return;
        }
        #endregion
    }
}
