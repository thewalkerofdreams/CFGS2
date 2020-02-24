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
    public class AddPersonVM : clsVMBase
    {
        private String _firstName;
        private String _lastName;
        private DateTime _dateOfBirth;
        private String _phone;
        private DateTime _actualDate;
        private ObservableCollection<clsDepartamento> _departamentList;
        private clsDepartamento _departamentSelected;
       
        public DelegateCommand SaveCommand { get; }

        public AddPersonVM()
        {
            cargarListadoDepartamentos();
            _firstName = "";
            _lastName = "";
            _dateOfBirth = new DateTime();
            _phone = "";
            this.SaveCommand = new DelegateCommand(ExecuteInsertCommand, CanExecuteSaveCommand);
            _actualDate = DateTime.Now;
            _departamentSelected = null;
        }

        #region Propiedades Públicas
        public String FirstName
        {
            get
            {
                return _firstName;
            }
            set
            {
                _firstName = value;
                NotifyPropertyChanged("FirstName");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }

        public String LastName
        {
            get
            {
                return _lastName;
            }
            set
            {
                _lastName = value;
                NotifyPropertyChanged("LastName");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }

        public DateTime DateOfBirth
        {
            get
            {
                return _dateOfBirth;
            }
            set
            {
                _dateOfBirth = value;
                NotifyPropertyChanged("DateOfBirth");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }

        public String Phone
        {
            get
            {
                return _phone;
            }
            set
            {
                _phone = value;
                NotifyPropertyChanged("Phone");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }

        public DateTime ActualDate
        {
            get
            {
                return _actualDate;
            }
        }

        public ObservableCollection<clsDepartamento> DepartamentList
        {
            get
            {
                return _departamentList;
            }
        }

        public clsDepartamento DepartamentSelected
        {
            get
            {
                return _departamentSelected;
            }
            set
            {
                _departamentSelected = value;
                NotifyPropertyChanged("DepartamentSelected");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }
        #endregion

        #region Commands
        /// <summary>
        /// Comentario: Este método nos permite insertar una nueva persona en la base de datos. 
        /// </summary>
        private async void ExecuteInsertCommand()
        {
            var answer = await Application.Current.MainPage.DisplayAlert("Add", "Do you want to add this person?", "Yes", "No");

            if (answer)
            {
                clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();
                clsPersona personaACrear = new clsPersona();
                personaACrear.nombrePersona = _firstName;
                personaACrear.apellidosPersona = _lastName;
                personaACrear.fechaNacimientoPersona = _dateOfBirth;
                personaACrear.telefonoPersona = _phone;
                personaACrear.idDepartamento = _departamentSelected.Id;
                try
                {

                    if (await gestionPersonasBl.insertarPersonaAsync(personaACrear) == 1)
                    {
                        exitoInsercion();
                    }
                    else
                    {
                        falloInsercion();
                    }

                }
                catch (Exception)
                {
                    falloConexion();
                }

            }

        }

        /// <summary>
        /// Comentario: Este método nos permite verificar si podemos ejecutar el comando de insertar una persona.
        /// </summary>
        /// <returns></returns>
        private bool CanExecuteSaveCommand()
        {
            bool habilitado = true;
            if (_firstName.Equals("") || _lastName.Equals("") || _phone.Equals("") || _dateOfBirth.Equals(new DateTime()) || _departamentSelected == null)
            {
                habilitado = false;
            }
            return habilitado;
        }
        #endregion

        #region Funciones Listado
        /// <summary>
        /// Comentario: Este método nos permite cargar la lista de departamentos de la base de datos.
        /// </summary>
        private async void cargarListadoDepartamentos()
        {
            try
            {
                _departamentList = new ObservableCollection<clsDepartamento>(await new clsListadosDepartamentosBL().listadoCompletoDepartamentos());
                NotifyPropertyChanged("DepartamentList");
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
                   "Error en la conexión",
                   "OK"
                   );

            return;
        }

        private async void falloInsercion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "No se puedo insertar la persona!",
                   "OK"
                   );

            return;
        }

        private async void exitoInsercion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Exito",
                   "Persona Insertada!",
                   "OK"
                   );

            return;
        }
        #endregion
    }
}
