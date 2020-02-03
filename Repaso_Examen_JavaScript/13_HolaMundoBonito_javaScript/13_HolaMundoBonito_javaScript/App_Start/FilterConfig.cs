using System.Web;
using System.Web.Mvc;

namespace _13_HolaMundoBonito_javaScript
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
