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
            BindingContext = new VMMainPage();
            viewModel = (VMMainPage) BindingContext;
        }

        private async void Button_Clicked(object sender, EventArgs e)//nos direcciona a la pádina de creación
        {
            await Navigation.PushAsync(new CRUDXamarin.Views.AddPerson());
        }

        private async void Button_Clicked_1(object sender, EventArgs e)//nos direcciona a la pádina de edición
        {
            await Navigation.PushAsync(new CRUDXamarin.Views.EditPerson(viewModel.PersonaSeleccionada));
        }

        private async void Button_Clicked_2(object sender, EventArgs e)//nos direcciona a la pádina de detalles
        {
            await Navigation.PushAsync(new CRUDXamarin.Views.DetailsPerson(viewModel.PersonaSeleccionada));
        }

        protected override void OnAppearing()//Cuando volvemos a la página recargaremos la lista de personas
        {
            base.OnAppearing();
            viewModel.cargarListados();
        }
    }
}
