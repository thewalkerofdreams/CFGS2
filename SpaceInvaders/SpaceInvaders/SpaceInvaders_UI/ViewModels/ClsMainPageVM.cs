using SpaceInvaders_Entities;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;

namespace SpaceInvaders_UI.ViewModels
{
    public class ClsMainPageVM : INotifyPropertyChanged
    {
        private ClsNave _ship;
        private DispatcherTimer _dispatcherTimer { get; set; }//Nos permite llamar continuamente a métodos

        #region Constructores
        public ClsMainPageVM()
        {
            _ship = new ClsNave();
            _dispatcherTimer = new DispatcherTimer();
            _dispatcherTimer.Interval = new TimeSpan(0, 0, 0, 0, 1);//Intervalo en el que se va a ejecutar
            _dispatcherTimer.Tick += timerTick;//Método al que va a llamar
        }
        #endregion

        #region Propiedades públicas
        public ClsNave Ship
        {
            get
            {
                return _ship;
            }
            set
            {
                _ship = value;
                NotifyPropertyChanged("ClsNave");
            }
        }
        #endregion

        #region Movimiento Personaje
        /// <summary>
        /// Comentario: Este método nos permite mover la nave adyascentemente. 
        /// Si se ha alcanzado algún límite de la pantalla, la nave no se moverá.
        /// Entrada:
        ///    -char direccion
        /// Precondiciones:
        ///     -direccion debe ser igual a 'u'(up), 'd'(down), 'l'(left) o 'r'(right).
        /// Postcondiciones: El método mueve la nave si no se intenta sobrepasar algún límite de la pantalla.    
        /// </summary>
        public void move(char direccion)
        {
            switch (direccion)
            {
                case 'u':
                    if ((_ship.Position.Y - _ship.Speed) >= 0)
                        _ship.Position.Y -= _ship.Speed;
                    break;
                case 'd':
                    if ((_ship.Position.Y + _ship.Speed) <= 400)
                        _ship.Position.Y += _ship.Speed;
                    break;
                case 'l':
                    if ((_ship.Position.X - _ship.Speed) >= 0)
                        _ship.Position.X -= _ship.Speed;
                    break;
                case 'r':
                    if ((_ship.Position.X + _ship.Speed) <= 600)
                        _ship.Position.X += _ship.Speed;
                    break;
            }

            _ship.LastMovement = direccion;
            NotifyPropertyChanged("Ship");
        }
        #endregion

        #region TimerTick
        private void timerTick(object sender, object e)
        {
            move(_ship.LastMovement);
        }
        #endregion

        #region Colisiones con la nave
        
        #endregion

        #region Eventos
        /// <summary>
        /// Evento que se da al pulsar una tecla.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void Grid_KeyDown(object sender, KeyRoutedEventArgs e)
        {
            switch (e.Key)
            {
                case VirtualKey.A:
                    _ship.LastMovement = 'l';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.D:
                    _ship.LastMovement = 'r';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.W:
                    _ship.LastMovement = 'u';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.S:
                    _ship.LastMovement = 'd';
                    _dispatcherTimer.Start();
                    break;
            }
            NotifyPropertyChanged("Character");
        }

        /// <summary>
        /// Evento que se da al levantar una tecla.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void Grid_KeyUp(object sender, KeyRoutedEventArgs e)
        {
            if (e.Key == VirtualKey.A || e.Key == VirtualKey.D || e.Key == VirtualKey.W || e.Key == VirtualKey.S)
            {
                _dispatcherTimer.Stop();
            }
        }
        #endregion

        #region PropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;
        // Create the OnPropertyChanged method to raise the event
        private void NotifyPropertyChanged([CallerMemberName] String propertyName = "")
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}
