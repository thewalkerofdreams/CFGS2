using Animations_Space_DI_Entities;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Input;

namespace Animations_Space_DI_UI.ViewModels
{
    public class MainPageVM : INotifyPropertyChanged
    {
        private ClsNave _nave;
        private DispatcherTimer _dispatcherTimer { get; set; }//Nos permite llamar continuamente a métodos

        public MainPageVM()
        {
            _nave = new ClsNave("/Assets/nave2.png", 300, 10);
            _dispatcherTimer = new DispatcherTimer();
            _dispatcherTimer.Interval = new TimeSpan(0, 0, 0, 0, 1);//Intervalo en el que se va a ejecutar
            _dispatcherTimer.Tick += timerTick;//Método al que va a llamar
        }

        #region Propiedades Públicas
        public ClsNave Nave
        {
            get
            {
                return _nave;
            }
            set
            {
                _nave = value;
                NotifyPropertyChanged("Nave");
            }
        }
        #endregion

        private void timerTick(object sender, object e)
        {
            if (_nave.LastMovement == 'd')
            {
                move('d');//La función que nos va a permitir mover la nave 
            }
            else
            {
                move('i');
            }
        }

        /// <summary>
        /// Comentario: Este método nos permite mover la nave hacia la derecha o hacia la izquierda. 
        /// Si se ha alcanzado algún límite de la pantalla, la nave no se moverá.
        /// Entrada:
        ///    -char direccion
        /// Precondiciones:
        ///     -direccion debe ser igual a 'i' o 'd'.
        /// Postcondiciones: El método mueve la nave si no se intenta sobrepasar algún límite de la pantalla.    
        /// </summary>
        public void move(char direccion)
        {
            int posicion;
            if (direccion == 'i')
            {
                posicion = _nave.XPosition - _nave.Movimiento;
                _nave.LastMovement = 'i';
            }
            else
            {
                posicion = _nave.XPosition + _nave.Movimiento;
                _nave.LastMovement = 'd';
            }
            NotifyPropertyChanged("Nave");

            if (posicion >= 0 && posicion <= 600)
            {
                _nave.XPosition = posicion;
                NotifyPropertyChanged("Nave");
            }
        }

        #region Eventos
        /// <summary>
        /// Evento que se da al pulsar una tecla, en este caso, A y D para mover la barra.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void Grid_KeyDown(object sender, KeyRoutedEventArgs e)
        {
            if (e.Key == VirtualKey.A)
            {
                move('i');
                _dispatcherTimer.Start();
            }

            if (e.Key == VirtualKey.D)
            {
                move('d');
                _dispatcherTimer.Start();
            }

        }

        /// <summary>
        /// Evento que se da al levantar una tecla, en este caso, A o D para parar el movimiento de la nave.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void Grid_KeyUp(object sender, KeyRoutedEventArgs e)
        {
            if (e.Key == VirtualKey.A || e.Key == VirtualKey.D)
            {
                _dispatcherTimer.Stop();
            }
        }
        #endregion
        #region PropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;

        protected virtual void NotifyPropertyChanged(string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}
