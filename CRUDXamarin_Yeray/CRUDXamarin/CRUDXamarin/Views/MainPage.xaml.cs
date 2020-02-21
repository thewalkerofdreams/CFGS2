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
        public MainPage()
        {
            InitializeComponent();
            BindingContext = new VMMainPage();
        }

        private async void Button_Clicked(object sender, EventArgs e)
        {
            //this.main = new NavigationPage(new CRUDXamarin.Views.AddPerson());
            await Navigation.PushAsync(new CRUDXamarin.Views.AddPerson());
        }
    }
}
