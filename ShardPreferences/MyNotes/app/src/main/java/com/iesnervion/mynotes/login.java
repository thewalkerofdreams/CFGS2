package com.iesnervion.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iesnervion.mynotes.Utilidades.LanguageHelper;

public class login extends AppCompatActivity {
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = this.getSharedPreferences("idiomas", MODE_PRIVATE);
        String idioma = prefs.getString("idioma","es"); //Miramos el valor de la clave Idioma, si no hay nada le asigna por defecto lo del segundo parametro, en este caso "es"

        LanguageHelper.setAppLocale(idioma, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Obtenemos las vistas.
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Si el usuario esta logueado, entra a ver sus notas.
        if(user != null){
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

    /**
     * Comprueba las credenciales que el usuario ha introducido.
     * Si son correctas puede entrar en la aplicacion. Sino, se le mostrará un mensaje por pantalla.
     * @param v Vista sobre la que se ha pulsado, en este caso sobre el boton de login.
     */
    public void comprobarCredenciales(View v){
        String correo, pass;
        //Si los campos no estan vacios
        if(!(correo = email.getText().toString().trim()).isEmpty() && !(pass = password.getText().toString().trim()).isEmpty()){
            mAuth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Si se ha logueado correctamente...
                        Toast.makeText(getApplicationContext(), R.string.toastWelcome, Toast.LENGTH_SHORT).show();

                        //Lanzamos la actividad principal
                        finish();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        //Si el inicio de sesion falla mostramos un mensaje.
                        Toast.makeText(getApplicationContext(), R.string.toastError, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), R.string.camposVacios, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Abre la actividad CreateAccount
     * @param v Vista sobre la que se ha pulsado, en este caso el boton crear cuenta.
     */
    public void navigateToCreateAccount(View v){
        //Lanzamos la actividad de crear cuenta.
        Intent i = new Intent(getApplicationContext(), createaccount.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
