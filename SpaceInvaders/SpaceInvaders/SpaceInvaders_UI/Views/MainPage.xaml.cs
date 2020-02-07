using SpaceInvaders_Entities;
using SpaceInvaders_UI.ViewModels;
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
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace SpaceInvaders_UI
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
            for (int i = 0; i < 35; i++)
                lanzarAsteroide();
            
        }

        private void allowfocus_Loaded(object sender, RoutedEventArgs e)
        {
            Window.Current.Content.KeyDown += this.mainPageVM.Grid_KeyDown;
            Window.Current.Content.KeyUp += this.mainPageVM.Grid_KeyUp;
        }

        private void lanzarAsteroide()
        {
            Canvas01.RenderTransform = new CompositeTransform();
            //Creamos la estrella
            Random random = new Random();
            ClsAsteroide asteroide = new ClsAsteroide("ms-appx:///Assets/asteroide01.gif", new ClsPunto(random.Next(1, 680), 0), random.Next(2, 6) / 3.0);

            //Create a image
            Image image = new Image();
            BitmapImage imgArt = new BitmapImage(new Uri(asteroide.Image, UriKind.Absolute));
            image.Source = imgArt;
            image.Height = asteroide.Size * 10;
            image.Width = asteroide.Size * 10;

            //Create the transform
            TranslateTransform moveTransform = new TranslateTransform();
            moveTransform.X = asteroide.Position.X;
            moveTransform.Y = 0;
            image.RenderTransform = moveTransform;

            //Le agregamos la ellipse al canvas
            Canvas01.Children.Add(image);

            // Create a duration of 2 seconds.
            Duration duration = new Duration(TimeSpan.FromSeconds(2.5 - asteroide.Size));

            // Create two DoubleAnimations and set their properties.
            DoubleAnimation myDoubleAnimationY = new DoubleAnimation();
            myDoubleAnimationY.Duration = duration;
            Storyboard justintimeStoryboard = new Storyboard();
            justintimeStoryboard.Duration = duration;
            justintimeStoryboard.Children.Add(myDoubleAnimationY);
            Storyboard.SetTarget(myDoubleAnimationY, moveTransform);

            // Set the Y property of the Transform to be the target property
            // of the respective DoubleAnimation.
            Storyboard.SetTargetProperty(myDoubleAnimationY, "Y");
            myDoubleAnimationY.To = 500 - 20;

            // Make the Storyboard a resource.
            //Canvas01.Resources.Add("justintimeStoryboard", justintimeStoryboard);
            // Begin the animation.
            justintimeStoryboard.Completed += (sender, e) =>
            {
                Canvas01.Children.Remove(image);
                lanzarAsteroide();
            };

            justintimeStoryboard.Begin();
        }
    }
}
