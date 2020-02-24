using CRUDXamarin_BL.Handler;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Forms;

namespace CRUDXamarin.viewModels
{
    public class DetailsPersonVM : clsVMBase
    {
        private clsPersona _personaToSee;
        private clsDepartamento _personsDepartament;

        #region Constructores
        public DetailsPersonVM()
        {
            _personaToSee = null;
            _personsDepartament = null;
        }

        public DetailsPersonVM(clsPersona persona)
        {
            _personaToSee = persona;
            getPersonDepartament();
        }
        #endregion

        #region Propiedades Públicas
        public clsPersona PersonToSee
        {
            get
            {
                return _personaToSee;
            }
        }

        public clsDepartamento PersonsDepartament
        {
            get
            {
                return _personsDepartament;
            }
        }
        #endregion

        #region Métodos de Gestión
        private async void getPersonDepartament()
        {
            try
            {
                _personsDepartament = await new clsGestionDepartamentosBL().obtenerDepartamento(_personaToSee.idDepartamento);
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
        #endregion
    }
}
