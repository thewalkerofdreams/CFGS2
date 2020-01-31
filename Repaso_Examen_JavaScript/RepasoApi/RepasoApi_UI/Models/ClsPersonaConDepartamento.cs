using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RepasoApi_Entities;
using RepasoApi_BL;
using RepasoApi_BL.Handlers;

namespace RepasoApi_UI.Models
{
    public class ClsPersonaConDepartamento : ClsPersona
    {
        private ClsDepartamento _departament;

        public ClsPersonaConDepartamento(ClsPersona persona) : base(persona.id, persona.nombre, persona.apellidos, persona.telefono, persona.fechaNacimiento, persona.idDepartamento, persona.fotoPersona)
        {
            ClsHandlerDepartamento_BL clsDepartamentoHandler_BL = new ClsHandlerDepartamento_BL();
            _departament = clsDepartamentoHandler_BL.obtenerDepartamento(persona.idDepartamento);
        }

        public ClsDepartamento Departament
        {
            get
            {
                return _departament;
            }
            set
            {
                _departament = value;
            }
        }
    }
}