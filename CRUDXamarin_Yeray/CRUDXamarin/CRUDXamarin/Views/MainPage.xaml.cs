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

        private async void Button_Clicked(object sender, EventArgs e)
        {
            //this.main = new NavigationPage(new CRUDXamarin.Views.AddPerson());
            await Navigation.PushAsync(new CRUDXamarin.Views.AddPerson());
        }

        private async void Button_Clicked_1(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new CRUDXamarin.Views.EditPerson(viewModel.PersonaSeleccionada));
        }

        private async void Button_Clicked_2(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new CRUDXamarin.Views.DetailsPerson(viewModel.PersonaSeleccionada));
        }
    }
}
