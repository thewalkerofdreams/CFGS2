using CRUDXamarin_BL.Handler;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
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
        private int _departamentId;
       
        public DelegateCommand SaveCommand { get; }

        public AddPersonVM()
        {
            _firstName = "";
            _lastName = "";
            _dateOfBirth = new DateTime();
            _phone = "";
            _departamentId = 0;
            this.SaveCommand = new DelegateCommand(ExecuteInsertCommand, CanExecuteDeleteCommand);
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

        public int DepartamentId
        {
            get
            {
                return _departamentId;
            }
            set
            {
                _departamentId = value;
                NotifyPropertyChanged("DepartamentId");
                SaveCommand.RaiseCanExecuteChanged();
            }
        }
        #endregion

        #region Commands
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
                personaACrear.idDepartamento = _departamentId;
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

        private bool CanExecuteDeleteCommand()
        {
            bool habilitado = true;
            if (_firstName.Equals("") || _lastName.Equals("") || _phone.Equals("") /*|| _dateOfBirth.Equals(new DateTime())*/ || _departamentId == 0)
            {
                habilitado = false;
            }
            return habilitado;
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
