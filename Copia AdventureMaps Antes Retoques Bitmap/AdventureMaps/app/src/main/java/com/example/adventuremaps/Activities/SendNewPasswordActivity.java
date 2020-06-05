package com.example.adventuremaps.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.SendNewPasswordActivityVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SendNewPasswordActivity extends AppCompatActivity {

    private EditText textEmail;
    private SendNewPasswordActivityVM viewModel;
    private FirebaseAuth myFireBaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_password);

        //Inicializamos el VM
        viewModel = ViewModelProviders.of(this).get(SendNewPasswordActivityVM.class);

        //Incializamos las views
        textEmail = findViewById(R.id.EditTextEmailActivitySendNewPassword);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla, en landscape eliminamos el icono de la aplicación
            LinearLayout linearLayout = findViewById(R.id.ImageViewSendNewPasswordActivity);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, (float) 1.0);
            param.weight = 50;//Aumentamos el tamaño que ocupa en la actividad
            param.gravity = Gravity.CENTER;//Lo centramos
            linearLayout.setLayoutParams(param);
        }
    }

    /**
     * Interfaz
     * Nombre: trySendNewPassword
     * Comentario: Este método intentará enviar un mensaje para el cambio de contraseña a un correo en específico,
     * si el campo se encuentra vacío o surge algún error de conexión el método informa de ello, mostrando
     * un mensaje de error por pantalla.
     * Cabecera: public void trySendNewPassword(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método envía un mensaje para el cambio de contraseña a un correo o manda un mensaje de
     * error por pantalla.
     */
    public void trySendNewPassword(View v){
        viewModel.set_email(textEmail.getText().toString().trim());

        if(!viewModel.get_email().isEmpty()){//Si el campo del correo no se encuentra vacío
            myFireBaseAuth.sendPasswordResetEmail(viewModel.get_email())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){//Si se pudo enviar el mensaje
                                Toast.makeText(getApplication(), R.string.sent_new_password, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplication(), R.string.send_new_password_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
        }
    }
}
