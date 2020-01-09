package es.iesnervion.yeray.piedrapapeltijera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        imageView = findViewById(R.id.imageView01);
    }

    /*
    * Interfaz
    * Nombre: playGame
    * Comentario: Este método nos permite jugar una partida de piedra, papel o tijeras contra la máquina.
    * El método almacenará también el resultado de la partida y mostrará un mensaje final para indicar
    * el ganador de la partida.
    * Cabecera: public void playGame(View v)
    * Entrada:
    *   -View v
    * Postcondiciones: El método devuelve un mensaje por pantalla indicando el resultado de la partida.
    * */
    public void playGame(View v){
        Random random = new Random();
        int toolMachine = random.nextInt(3)+1, aux = 0;
        switch (toolMachine){//Modificamos la imagen principal para saber que ha sacado la máquina
            case 1:
                imageView.setImageResource(R.drawable.stone);
                break;
            case 2:
                imageView.setImageResource(R.drawable.plane);
                break;
            case 3:
                imageView.setImageResource(R.drawable.scissor);
                break;
        }
        String mensaje = "";
        //SharedPreferences sharedPreferences = this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//Nos permite compartir una misma ShaderPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();//Lo declaramos solo una vez para ahorrar código

        switch (v.getId()){
            case R.id.btnStone:
                if(toolMachine == 1){
                    mensaje = "Tie";
                }else{
                    if(toolMachine == 2){
                        mensaje = "You lose!";
                        aux = sharedPreferences.getInt("PartidasPerdidas", 0);//La variable auxiliar es obligatoria...
                        editor.putInt("PartidasPerdidas", ++aux);
                    }else{
                        mensaje = "You Win!";
                        aux = sharedPreferences.getInt("PartidasGanadas", 0);
                        editor.putInt("PartidasGanadas", ++aux);
                    }
                }
                aux = sharedPreferences.getInt("PiedrasJugadas", 0);
                editor.putInt("PiedrasJugadas", ++aux);
                break;
            case R.id.btnPaper:
                if(toolMachine == 1){
                    mensaje = "You Win!";
                    aux = sharedPreferences.getInt("PartidasGanadas", 0);
                    editor.putInt("PartidasGanadas", ++aux);
                }else{
                    if(toolMachine == 2){
                        mensaje = "Tie";
                    }else{
                        mensaje = "You lose!";
                        aux = sharedPreferences.getInt("PartidasPerdidas", 0);
                        editor.putInt("PartidasPerdidas", ++aux);
                    }
                }
                aux = sharedPreferences.getInt("PapelesJugados", 0);
                editor.putInt("PapelesJugados", ++aux);
                break;
            case R.id.btnScissors:
                if(toolMachine == 1){
                    mensaje = "You lose!";
                    aux = sharedPreferences.getInt("PartidasPerdidas", 0);
                    editor.putInt("PartidasPerdidas", ++aux);
                }else{
                    if(toolMachine == 2){
                        mensaje = "You Win!";
                        aux = sharedPreferences.getInt("PartidasGanadas", 0);
                        editor.putInt("PartidasGanadas", ++aux);

                    }else{
                        mensaje = "Tie";
                    }
                }
                aux = sharedPreferences.getInt("TijerasJugadas", 0);
                editor.putInt("TijerasJugadas", ++aux);
                break;
        }

        editor.commit();//Comiteamos los posibles cambios
        Toast.makeText(getApplicationContext(),mensaje,Toast. LENGTH_SHORT).show();//Indicamos el resultado de la partida
    }

}
