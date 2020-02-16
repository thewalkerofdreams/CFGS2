using CRUDXamarin_BL.Handler;
using CRUDXamarin_BL.List;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace CRUDXamarin.viewModels
{
    public class VMMainPage : clsVMBase
    {
        private ObservableCollection<clsPersona> listadoPersonas;
        private clsPersona _personaSeleccionada;
        private clsPersona _personaAux { get; set; }

        #region Cosntructores
        public VMMainPage()
        {
            cargarListados();
            this._personaSeleccionada = new clsPersona();
            this._personaAux = new clsPersona();
            this.DeleteCommand = new DelegateCommand(ExecuteDeleteCommand, CanExecuteDeleteCommand);
        }
        #endregion

        #region Propiedades Públicas
        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get {
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

        private async void ExecuteDeleteCommand()
        {
            var answer = await Application.Current.MainPage.DisplayAlert("Delete", "Do you want to delete this element?", "Yes", "No");
          
            if (answer)
            {
                clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
                try
                {
                    if (await gestionPersonasBl.eliminarPersonaAsync(this._personaSeleccionada.idPersona) == 1)
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

        private bool CanExecuteDeleteCommand()
        {
            bool habilitado = true;
            if (this._personaSeleccionada == null)
            {
                habilitado = false;
            }
            return habilitado;
        }

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
                        clsGestionPersonasBL gestionPersonasBL = new clsGestionPersonasBL();
                        Task<clsPersona> task = gestionPersonasBL.obtenerPersona(this._personaSeleccionada.idPersona);
                        task.Wait();
                        clsPersona personaReal = task.Result;
                    }

                }
                catch (Exception e)
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
                }
            }
        }
        #endregion

        #region Métodos Listados
        private async void cargarListados()
        {
            try
            {
                this.listadoPersonas = new ObservableCollection<clsPersona>(await new clsListadosPersonaBL().listadoCompletoPersonas());
                NotifyPropertyChanged("ListadoPersonas");
            }
            catch (Exception e)
            {
                falloConexion();
            }

        }
        #endregion

        #region Mensajes
        /// <summary>
        /// Comentario: Este método nos permite mostrar una alerta por pantalla.
        /// </summary>
        private async void falloConexion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "Connection Fail",
                   "OK"
                   );

            return;
        }
        #endregion
    }
}
