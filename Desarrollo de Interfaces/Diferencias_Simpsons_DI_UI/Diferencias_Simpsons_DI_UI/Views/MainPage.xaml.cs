using Diferencias_Simpsons_DI_UI.ViewModels;
using OpenTK.Input;
using System;
using System.Collections.Generic;
using System.Drawing;
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
using Windows.UI.Xaml.Shapes;

// The Blank Page item template is documented at https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace Diferencias_Simpsons_DI_UI
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        public MainPageVM ViewModel { get; }//Necesitamos declarar un objeto VM en el code behind de la página para que los x:Bind funcionen.
        public MainPage()
        {
            this.InitializeComponent();
            this.ViewModel = new MainPageVM();
        }

        private void Ellipse_Tapped(object sender, TappedRoutedEventArgs e)
        {
            Ellipse ellipse = (Ellipse)sender;//Obtenemos la ellipse
            switch (ellipse.Name)//Modificaremos el estado de dos ellipses, según la ellipse pulsada
            {
                case "Ellipse01": case "Ellipse1":
                    if (Ellipse01.Opacity == 0)
                    {
                        Ellipse01.Opacity = 1.0;
                        Ellipse1.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse02": case "Ellipse2":
                    if (Ellipse02.Opacity == 0)
                    {
                        Ellipse02.Opacity = 1.0;
                        Ellipse2.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse03": case "Ellipse3":
                    if (Ellipse03.Opacity == 0)
                    {
                        Ellipse03.Opacity = 1.0;
                        Ellipse3.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse04": case "Ellipse4":
                    if (Ellipse04.Opacity == 0)
                    {
                        Ellipse04.Opacity = 1.0;
                        Ellipse4.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse05": case "Ellipse5":
                    if (Ellipse05.Opacity == 0)
                    {
                        Ellipse05.Opacity = 1.0;
                        Ellipse5.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse06": case "Ellipse6":
                    if (Ellipse06.Opacity == 0)
                    {
                        Ellipse06.Opacity = 1.0;
                        Ellipse6.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse07": case "Ellipse7":
                    if (Ellipse07.Opacity == 0)
                    {
                        Ellipse07.Opacity = 1.0;
                        Ellipse7.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
                case "Ellipse08": case "Ellipse8":
                    if (Ellipse08.Opacity == 0)
                    {
                        Ellipse08.Opacity = 1.0;
                        Ellipse8.Opacity = 1.0;
                        ViewModel.ContadorDiferencias++;
                    }
                    break;
            }
            
        }
    }
}
