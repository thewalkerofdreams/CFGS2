public class MyWaitNotify{

    private char buffer[];
    private int siguiente;
    // Flags para saber el estado del buffer
    private boolean estaLlena ;
    private boolean estaVacia;

    public MyWaitNotify(int cantidad){
        buffer = new char[cantidad];
        siguiente = 0;
        estaLlena = false;
        estaVacia = true;
    }

    public synchronized char recoger() {// Método para retirar letras del array
        while( estaVacia == true )//Si esta vacío ponemos el hilo en espera
        {
            try {
                wait();
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        // Decrementa la cuenta, ya que va a consumir una letra
        siguiente--;
        // Comprueba si se retiró la última letra
        if( siguiente == 0 )//Si hemos consumido la última letra
            estaVacia = true;

        estaLlena = false;//Como acabamos de consumir una letra es imposible que este llena
        notify();

        // Devuelve la letra al thread consumidor
        return( buffer[siguiente] );
    }

    // Método para añadir letras al buffer
    public synchronized void lanzar( char c ) {
        // Espera hasta que haya sitio para otra letra
        while( estaLlena == true )
        {
            try {
                wait(); // Se sale cuando estaLlena cambia a false
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        // Añade una letra en el primer lugar disponible
        buffer[siguiente] = c;
        // Cambia al siguiente lugar disponible
        siguiente++;
        // Comprueba si el buffer está lleno
        if( siguiente == 8 )
            estaLlena = true;
        estaVacia = false;
        notify();
    }
}