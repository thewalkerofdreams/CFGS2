using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Examen_DI_DAL.Connections
{
    public class ClsMyConnection
    {
        public static string getUriBase()
        {
            String uriBase = "https://crudtoflamaapi.azurewebsites.net/api";
            return uriBase;
        }
    }
}
