using CRUDXamarin.viewModels;
using CRUDXamarin_Ent;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CRUDXamarin.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DetailsPerson : ContentPage
    {
        public DetailsPerson(clsPersona persona)
        {
            InitializeComponent();
            BindingContext = new DetailsPersonVM(persona);
        }
    }
}