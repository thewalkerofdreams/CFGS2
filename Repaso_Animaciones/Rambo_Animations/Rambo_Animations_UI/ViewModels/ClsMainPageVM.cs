using Rambo_Animations_Entities;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Windows.System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Input;

namespace Rambo_Animations_UI.ViewModels
{
    public class ClsMainPageVM : INotifyPropertyChanged
    {
        private ClsPersonaje _character;
        private DispatcherTimer _dispatcherTimer { get; set; }//Nos permite llamar continuamente a métodos

        #region Constructores
        public ClsMainPageVM()
        {
            _character = new ClsPersonaje();
            _dispatcherTimer = new DispatcherTimer();
            _dispatcherTimer.Interval = new TimeSpan(0, 0, 0, 0, _character.Gun.Cadence);//Intervalo en el que se va a ejecutar
            _dispatcherTimer.Tick += timerTick;//Método al que va a llamar
        }
        #endregion

        #region Propiedades públicas
        public ClsPersonaje Character
        {
            get
            {
                return _character;
            }
            set
            {
                _character = value;
            }
        }
        #endregion

        #region Movimiento Personaje
        /// <summary>
        /// Comentario: Este método nos permite mover adyascentemente al personaje. 
        /// Si se ha alcanzado algún límite de la pantalla, el personaje no se moverá.
        /// Entrada:
        ///    -char direccion
        /// Precondiciones:
        ///     -direccion debe ser igual a 'u'(up), 'd'(down), 'l'(left) o 'r'(right).
        /// Postcondiciones: El método mueve al personaje si no se intenta sobrepasar algún límite de la pantalla.    
        /// </summary>
        public void move(char direccion)
        {
            switch (direccion)
            {
                case 'u':
                    if ((_character.Position.Y - _character.Speed) >= 0)
                        _character.Position.Y -= _character.Speed;
                    break;
                case 'd':
                    if ((_character.Position.Y + _character.Speed) <= 400)
                        _character.Position.Y += _character.Speed;
                    break;
                case 'l':
                    if ((_character.Position.X - _character.Speed) >= 0)
                        _character.Position.X -= _character.Speed;
                    break;
                case 'r':
                    if ((_character.Position.X + _character.Speed) <= 600)
                        _character.Position.X += _character.Speed;
                    break;
            }

            _character.LastMovement = direccion;
            NotifyPropertyChanged("Character");
        }
        #endregion

        #region TimerTick
        private void timerTick(object sender, object e)
        {
            move(_character.LastMovement);
        }
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
                    _character.LastMovement = 'l';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.D:
                    _character.LastMovement = 'r';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.W:
                    _character.LastMovement = 'u';
                    _dispatcherTimer.Start();
                    break;
                case VirtualKey.S:
                    _character.LastMovement = 'd';
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
