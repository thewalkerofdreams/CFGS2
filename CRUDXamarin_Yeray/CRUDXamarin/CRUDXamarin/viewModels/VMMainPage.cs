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

        public ObservableCollection<clsPersona> ListadoPersonas
        {
            get {

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

        public DelegateCommand GoToAddPageCommand { get; }

        #region Commands
        private async void ExecuteDeleteCommand()
        {
            var answer = await Application.Current.MainPage.DisplayAlert("Delete", "Do you want to delete this element?", "Yes", "No");
            if (answer)
            {
               //TODO
            }

            if (answer)
            {
                //Se procede a la borración
                //Aqui va el codigo de delete
                //this._listadoPersonas.Remove((this._personaSeleccionada)); //Esto no puede ser asi porque persona seleccionada no es un item de listado Personas
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
                    //SaveCommand.RaiseCanExecuteChanged();
          
                    NotifyPropertyChanged("PersonaSeleccionada");


                }
            }
        }


        public VMMainPage()
        {
            
            
            try
            {
                cargarListados();   //esto esta mal
                NotifyPropertyChanged("ListadoPersonas");
            }
            catch (Exception)
            {
                //Mostrar algo para cuando falle la conexion (ej- un dialog)

                falloConexion();

            }

            //this._personaSeleccionada = new clsPersona();
            this._personaAux = new clsPersona();
            this.DeleteCommand = new DelegateCommand(ExecuteDeleteCommand, CanExecuteDeleteCommand);
        }

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

        private async void falloConexion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "Alerta mannn",
                   "OK"
                   );

            return;
        }
    }
}
