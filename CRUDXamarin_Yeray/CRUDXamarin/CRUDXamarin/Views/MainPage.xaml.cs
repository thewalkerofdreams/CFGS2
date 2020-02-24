using CRUDXamarin.viewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace CRUDXamarin
{
    public partial class MainPage : ContentPage
    {
        public VMMainPage viewModel { get; set; }
        public MainPage()
        {
            InitializeComponent();
            BindingContext = new VMMainPage(Navigation);
            viewModel = (VMMainPage) BindingContext;
        }

        protected override void OnAppearing()//Cuando volvemos a la página recargaremos la lista de personas
        {
            base.OnAppearing();
            viewModel.cargarListados();
            viewModel.PersonaSeleccionada = null;
        }
    }
}
