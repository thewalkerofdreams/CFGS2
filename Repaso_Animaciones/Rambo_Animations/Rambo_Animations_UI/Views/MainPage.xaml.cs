using Rambo_Animations_UI.ViewModels;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace Rambo_Animations_UI
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        ClsMainPageVM mainPageVM { get; }
        public MainPage()
        {
            this.InitializeComponent();
            mainPageVM = (ClsMainPageVM)this.DataContext;
        }

        private void allowfocus_Loaded(object sender, RoutedEventArgs e)
        {
            Window.Current.Content.KeyDown += this.mainPageVM.Grid_KeyDown;
            Window.Current.Content.KeyUp += this.mainPageVM.Grid_KeyUp;
        }
    }
}
