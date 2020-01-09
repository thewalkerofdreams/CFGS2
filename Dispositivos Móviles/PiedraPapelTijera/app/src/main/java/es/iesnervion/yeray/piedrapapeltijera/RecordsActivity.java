package es.iesnervion.yeray.piedrapapeltijera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordsActivity extends AppCompatActivity {

    TextView partidasGanadas, partidasPerdidas, piedra, tijeras, papel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        partidasGanadas = findViewById(R.id.textViewWonGames);
        partidasPerdidas = findViewById(R.id.textViewLostGames);
        piedra = findViewById(R.id.textViewPlayedStone);
        papel = findViewById(R.id.textViewPlayedPaper);
        tijeras = findViewById(R.id.textViewPlayedScissor);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        partidasGanadas.setText(String.valueOf(sharedPreferences.getInt("PartidasGanadas", 0)));
        partidasPerdidas.setText(String.valueOf(sharedPreferences.getInt("PartidasPerdidas", 0)));
        piedra.setText(String.valueOf(sharedPreferences.getInt("PiedrasJugadas", 0)));
        papel.setText(String.valueOf(sharedPreferences.getInt("PapelesJugados", 0)));
        tijeras.setText(String.valueOf(sharedPreferences.getInt("TijerasJugadas", 0)));
    }


}
