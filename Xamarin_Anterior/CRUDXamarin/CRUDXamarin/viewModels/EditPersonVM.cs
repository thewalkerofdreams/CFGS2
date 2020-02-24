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
    public class EditPersonVM : clsVMBase
    {
        private clsPersona _personToMod;
        private DateTime _actualDate;
        private clsDepartamento _personsDepartament;
        private ObservableCollection<clsDepartamento> _departamentList;
        public DelegateCommand ModCommand { get; }

        #region Constructores
        public EditPersonVM()
        {
            cargarListadoDepartamentos();
            _personToMod = null;
            _actualDate = DateTime.Now;
            ModCommand = new DelegateCommand(ExecuteModCommand);
        }

        public EditPersonVM(clsPersona persona)
        {
            cargarListadoDepartamentos();
            _personToMod = persona;
            _actualDate = DateTime.Now;
            ModCommand = new DelegateCommand(ExecuteModCommand);
            getPersonDepartament();
        }
        #endregion

        #region Propiedades Públicas
        public clsPersona PersonToMod
        {
            get
            {
                return _personToMod;
            }
            set
            {
                _personToMod = value;
                ModCommand.RaiseCanExecuteChanged();//No funciona ya que estoy modificando sus propiedades... Como hacer esto desde este mismo VM sin hacer mal código?
                NotifyPropertyChanged("PersonToMod");
            }
        }

        public DateTime ActualDate
        {
            get
            {
                return _actualDate;
            }
        }

        public clsDepartamento PersonsDepartament
        {
            get
            {
                return _personsDepartament;
            }
            set{
                if (value != null)
                {
                    _personsDepartament = value;
                    NotifyPropertyChanged("PersonsDepartament");
                    _personToMod.idDepartamento = _personsDepartament.Id;
                    NotifyPropertyChanged("PersonToMod");
                }
            }
        }

        public ObservableCollection<clsDepartamento> DepartamentList
        {
            get
            {
                return _departamentList;
            }
        }
        #endregion

        #region Commands
        private async void ExecuteModCommand()
        {
            if (_personToMod != null && !_personToMod.nombrePersona.Equals("") && !_personToMod.apellidosPersona.Equals("") && !_personToMod.telefonoPersona.Equals("") &&
                !_personToMod.fechaNacimientoPersona.Equals(new DateTime()) && _personsDepartament != null)//Por ahora lo dejamos aquí
            {

                var answer = await Application.Current.MainPage.DisplayAlert("Add", "Do you want to modificate this person?", "Yes", "No");

                if (answer)
                {
                    clsGestionPersonasBL gestionPersonasBl = new clsGestionPersonasBL();

                    try
                    {

                        if (await gestionPersonasBl.actualizarPersona(_personToMod) == 1)
                        {
                            exitoModificacion();
                        }
                        else
                        {
                            falloModificacion();
                        }

                    }
                    catch (Exception)
                    {
                        falloConexion();
                    }

                }
            }
        }

        private bool CanExecuteModCommand()
        {
            bool habilitado = true;
            if (_personToMod == null || _personToMod.nombrePersona.Equals("") || _personToMod.apellidosPersona.Equals("") || _personToMod.telefonoPersona.Equals("") ||
                _personToMod.fechaNacimientoPersona.Equals(new DateTime()) || _personsDepartament == null)
            {
                habilitado = false;
            }
            return habilitado;
        }
        #endregion

        #region Funciones Listado
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

        #region Métodos de Gestión
        private async void getPersonDepartament()
        {
            try
            {
                _personsDepartament = await new clsGestionDepartamentosBL().obtenerDepartamento(_personToMod.idDepartamento);
                NotifyPropertyChanged("PersonsDepartament");
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

        private async void falloModificacion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Alert",
                   "No se puedo modificar la persona!",
                   "OK"
                   );

            return;
        }

        private async void exitoModificacion()
        {
            await Application.Current.MainPage.DisplayAlert(
                   "Exito",
                   "Persona Modificada!",
                   "OK"
                   );

            return;
        }
        #endregion
    }
}
