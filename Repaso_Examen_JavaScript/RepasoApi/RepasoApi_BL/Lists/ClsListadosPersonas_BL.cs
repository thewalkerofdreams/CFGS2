﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RepasoApi_Entities;
using RepasoApi_DAL.Lists;

namespace RepasoApi_BL.Lists
{
    public class ClsListadosPersonas_BL
    {
        /// <summary>
        /// Nombre: obtenerListadoDePersonas
        /// Comentario: Este método nos permite obtener un listado de las personas almacenadas en la base de datos.
        /// Cabecera: public List<ClsPersona> obtenerListadoDePersonas()
        /// </summary>
        /// <returns>Devuelve un list del tipo ClsPersona</returns>
        public List<ClsPersona> obtenerListadoDePersonas()
        {
            return new ClsListadosPersonas_DAL().obtenerListadoDePersonas();
        }
    }
}
