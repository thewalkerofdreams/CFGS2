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
            //getPersonDepartament();//Esto debería haber funcionado
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
                //ModCommand.RaiseCanExecuteChanged();//No funciona ya que modifico las propiedades de la persona, nunca va a entrar aquí
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
                    if (_personsDepartament != null)
                    {
                        _personToMod.idDepartamento = _personsDepartament.Id;
                        NotifyPropertyChanged("PersonToMod");
                    }
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
        /// <summary>
        /// Comentario: Este método nos permite modificar una persona en la base de datos.
        /// </summary>
        private async void ExecuteModCommand()
        {
            //Esta comprobación no puedo hacerla en este caso en el método CanExecuteModCommand
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
        /// <summary>
        /// Comentario: Este método nos va a permitir verificar si podemos ejecutar un comando. 
        /// </summary>
        /// <returns></returns>
        //private bool CanExecuteModCommand()
        //{
        //    bool habilitado = true;
        //    if (_personToMod == null || _personToMod.nombrePersona.Equals("") || _personToMod.apellidosPersona.Equals("") || _personToMod.telefonoPersona.Equals("") ||
        //        _personToMod.fechaNacimientoPersona.Equals(new DateTime()) || _personsDepartament == null)
        //    {
        //        habilitado = false;
        //    }
        //    return habilitado;
        //}
        #endregion

        #region Funciones Listado
        /// <summary>
        /// Comentario: Este método nos permite cargar el listado de todos los departamentos de la base de datos.
        /// </summary>
        private async void cargarListadoDepartamentos()
        {
            try
            {
                _departamentList = new ObservableCollection<clsDepartamento>(await new clsListadosDepartamentosBL().listadoCompletoDepartamentos());
                NotifyPropertyChanged("DepartamentList");
                clsDepartamento depAux = await new clsGestionDepartamentosBL().obtenerDepartamento(_personToMod.idDepartamento);
                Boolean encontrado = false;
                for (int i = 0; i < _departamentList.Count && !encontrado; i++)//Esto es de Pablo un poco modificado, No se muy bien porque solo funciona así.
                {
                    if (depAux.Id == _departamentList[i].Id)
                    {
                        _personsDepartament = _departamentList[i];
                        NotifyPropertyChanged("PersonsDepartament");
                        encontrado = true;
                    }
                }
            }
            catch (Exception)
            {
                falloConexion();
            }
        }
        #endregion

        #region Métodos de Gestión
        /// <summary>
        /// Comentario: Este método nos permite obtener el departamento de la persona a editar.
        /// </summary>
        private async void getPersonDepartament()//Actualmente no la utilizo
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
