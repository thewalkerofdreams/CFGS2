package com.example.adventuremaps.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

        //Incializamos los views
        textEmail = findViewById(R.id.EditTextEmailActivitySendNewPassword);
    }

    /**
     * Interfaz
     * Nombre: trySendNewPassword
     * Comentario: Este método intentará mandar una nueva contraseña a un correo, si el campo
     * se encuentra vacío o surge algún error de conexión el método informa de ello, mandando
     * un mensaje de error por pantalla.
     * Cabecera: public void trySendNewPassword(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método envía una nueva contraseña a un correo o manda un mensaje de
     * error por pantalla.
     */
    public void trySendNewPassword(View v){
        viewModel.set_email(textEmail.getText().toString().trim());

        if(!viewModel.get_email().isEmpty()){
            myFireBaseAuth.sendPasswordResetEmail(viewModel.get_email())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
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
