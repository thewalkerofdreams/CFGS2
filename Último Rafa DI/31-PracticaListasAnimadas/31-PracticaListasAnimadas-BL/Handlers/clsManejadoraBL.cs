using _31_PracticaListasAnimadas_DAL.Handlers;
using _31_PracticaListasAnimadas_ENTITIES;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_BL.Handlers
{
    public class clsManejadoraBL
    {
        /// <summary>
        /// Con este metodo llamamos a borrar persona de la api
        /// </summary>
        /// <param name="idPersona">El id de la persona a eliminar</param>
        public async Task borrarPersona(int idPersona)
        {
            clsManejadoraDAL manejadoraDAL = new clsManejadoraDAL();
            await manejadoraDAL.borrarPersona(idPersona);
        }

        public async Task anhadirPersona(clsPersona persona)
        {
            clsManejadoraDAL manejadoraDAL = new clsManejadoraDAL();
            await manejadoraDAL.anhadirPersona(persona);
        
        }
    }
}
