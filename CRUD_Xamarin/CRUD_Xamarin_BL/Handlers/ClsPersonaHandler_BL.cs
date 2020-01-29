using CRUD_Xamarin_DAL.Handlers;
using CRUD_Xamarin_Entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace CRUD_Xamarin_BL.Handlers
{
    public class ClsPersonaHandler_BL
    {
        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos. 
        /// </summary>
        /// <param name="nombre"></param>
        /// <param name="apellidos"></param>
        /// <param name="telefono"></param>
        /// <param name="idDepartamento"></param>
        /// <param name="fechaNacimiento"></param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si se ha conseguido insertar la nueva persona o false en caso contrario.</returns>
        public bool insertarPersona(String nombre, String apellidos, String telefono, DateTime fechaNacimiento, int idDepartamento)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.insertarPersona(nombre, apellidos, telefono, fechaNacimiento, idDepartamento);
        }

        /// <summary>
        /// Comentario: Este método nos permite insertar una persona en la base de datos. 
        /// </summary>
        /// <param name="persona">El tipo ClsPersona.</param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si se ha conseguido insertar la nueva persona o false en caso contrario.</returns>
        public bool insertarPersona(ClsPersona persona)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.insertarPersona(persona.nombre, persona.apellidos, persona.telefono, persona.fechaNacimiento, persona.idDepartamento);
        }

        /// <summary>
        /// Comentario: este método nos permite eliminar una persona de la base de datos.
        /// </summary>
        /// <param name="id"></param>
        /// <returns>El método devuelve un valor booleano asociado al nombre, true si ha conseguido eliminar la persona o false en caso contrario.</returns>
        public bool eliminarPersona(int id)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.eliminarPersona(id);
        }

        /// <summary>
        /// Comentario: Este método nos permite modificar una persona de la base de datos.
        /// </summary>
        /// <param name="id"></param>
        /// <param name="nombre"></param>
        /// <param name="apellidos"></param>
        /// <param name="telefono"></param>
        /// <param name="fechaNacimiento"></param>
        /// <param name="idDepartamento"></param>
        /// <returns></returns>
        public bool editarPersona(int id, String nombre, String apellidos, String telefono, DateTime fechaNacimiento, int idDepartamento)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.editarPersona(id, nombre, apellidos, telefono, fechaNacimiento, idDepartamento);
        }

        /// <summary>
        /// Comentario: Este método nos permite modificar una persona de la base de datos.
        /// </summary>
        /// <param name="persona">El tipo ClsPersona</param>
        /// <returns></returns>
        public bool editarPersona(ClsPersona persona)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.editarPersona(persona);
        }

        /// <summary>
        /// Comentario: Este método nos permite obtener una persona de la base de datos.
        /// </summary>
        /// <param name="id"></param>
        /// <returns>El método devuelve un tipo ClsPersona asociado al nombre, que es la persona buscada o null si esa persona no existe en la base de datos.</returns>
        public ClsPersona obtenerPersona(int id)
        {
            ClsPersonaHandler_DAL clsPersonaHandler_DAL = new ClsPersonaHandler_DAL();
            return clsPersonaHandler_DAL.obtenerPersona(id);
        }
    }
}
