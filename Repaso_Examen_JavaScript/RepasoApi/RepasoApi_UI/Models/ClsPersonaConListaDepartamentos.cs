using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RepasoApi_Entities;
using RepasoApi_BL.Lists;

namespace RepasoApi_UI.Models
{
    public class ClsPersonaConListaDepartamentos : ClsPersona
    {
        private List<ClsDepartamento> _listadoDepartamentos;

        #region Constructores
        public ClsPersonaConListaDepartamentos() : base()
        {
            _listadoDepartamentos = new ClsListadosDepartamentos_BL().obtenerListadoDeDepartamentos();
        }

        public ClsPersonaConListaDepartamentos(ClsPersona persona) : base(persona.id, persona.nombre, persona.apellidos, persona.telefono, persona.fechaNacimiento, persona.idDepartamento, persona.fotoPersona)
        {
            _listadoDepartamentos = new ClsListadosDepartamentos_BL().obtenerListadoDeDepartamentos();
        }

        public ClsPersonaConListaDepartamentos(String nombre, String apellidos, DateTime fechaNacimiento, String telefono, int idDepartamento) : base(0, nombre, apellidos, telefono, fechaNacimiento, idDepartamento)
        {
            _listadoDepartamentos = new ClsListadosDepartamentos_BL().obtenerListadoDeDepartamentos();
        }

        #endregion

        public List<ClsDepartamento> ListadoDepartamentos
        {
            get
            {
                return _listadoDepartamentos;
            }
            set
            {
                _listadoDepartamentos = value;
            }
        }
    }
}