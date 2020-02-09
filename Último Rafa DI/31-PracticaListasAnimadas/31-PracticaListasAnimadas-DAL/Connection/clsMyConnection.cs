using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _31_PracticaListasAnimadas_DAL.Connection
{
    public class clsMyConnection
    {
        public static string getUriBase()
        {
            //String uriBase = "https://crudtoflamaapi.azurewebsites.net/api";
            String uriBase = "https://crudpersonasui-victor.azurewebsites.net/api/";
            return uriBase;
        }
    }
}
