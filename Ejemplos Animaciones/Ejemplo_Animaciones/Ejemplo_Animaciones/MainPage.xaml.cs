using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Xaml.Shapes;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace Ejemplo_Animaciones
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        public MainPage()
        {
            this.InitializeComponent();
        }

        private void myRectangle_PointerReleased(object sender, PointerRoutedEventArgs e)
        {
            myRectangle.Margin = new Thickness(400, 0, 0, 0);//Aquí le damos un margen al rectangulo que antes no tenía
            PointerReleasedStoryboard.Begin();//Esta animación nos va a permitir mover el rectangulo a su posición inicial por unos segundos
        }

        private void animatedButton_Click(object sender, RoutedEventArgs e)
        {
            animatedButton.Margin = new Thickness(100);    
        }

        private void RemoveButton_Click(object sender, RoutedEventArgs e)
        {
            if (rectangleItems.Items.Count > 0)
                rectangleItems.Items.RemoveAt(0);
        }

        private void AddButton_Click(object sender, RoutedEventArgs e)
        {
            Color rectColor = new Color();
            rectColor.R = 200;
            rectColor.A = 250;
            Rectangle myRectangle = new Rectangle();
            myRectangle.Fill = new SolidColorBrush(rectColor);
            myRectangle.Width = 100;
            myRectangle.Height = 100;
            myRectangle.Margin = new Thickness(10);
            rectangleItems.Items.Add(myRectangle);
        }
    }
}
