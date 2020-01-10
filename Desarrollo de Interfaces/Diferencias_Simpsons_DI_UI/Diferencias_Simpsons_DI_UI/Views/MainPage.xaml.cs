using OpenTK.Input;
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
using Windows.UI.Xaml.Shapes;

// The Blank Page item template is documented at https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace Diferencias_Simpsons_DI_UI
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        Ellipse ellipse;
        public MainPage()
        {
            this.InitializeComponent();
            //ellipse = new Ellipse();
            //ellipse.
            //ellipse. += ellipse_MouseUp;
        }

        private void Ellipse01_Tapped(object sender, TappedRoutedEventArgs e)
        {
            Ellipse ellipse = (Ellipse)sender;//Obtenemos la ellipse
            ellipse.Opacity= 1.0;//Ya no es invisible
        }
    }
}
