package com.example.adventuremaps.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.ChangePasswordActivityVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText textEmail, textOldPassword, textNewPassword01, textNewPassword02;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ChangePasswordActivityVM viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(ChangePasswordActivityVM.class);

        //Instanciamos las Views
        textEmail = findViewById(R.id.EditTextEmailActivityChangePassword);
        textOldPassword = findViewById(R.id.EditTextOldPasswordActivityChangePassword);
        textNewPassword01 = findViewById(R.id.EditTextNewPasswor01dActivityChangePassword);
        textNewPassword02 = findViewById(R.id.EditTextNewPasswor02dActivityChangePassword);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla en landscape
            LinearLayout icono = findViewById(R.id.IconImageChangePasswordActivity);
            icono.setVisibility(View.GONE);//Ocultamos la imagen del icono
        }
    }

    /**
     * Interfaz
     * Nombre: tryChangePassword
     * Comentario: Este método intentará cambiar la contraseña de una cuenta, si la cuenta existía
     * anteriormente en la plataforma y si todos los campos se rellenarón adecuadamente, en caso contrario
     * el método mostrará por pantalla un mensaje de error.
     * Cabecera: public void tryChangePassword(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método cambia la contraseña de una cuenta o muestra por pantalla un mensaje de
     * error si alguno de los campos no es válido.
     */
    public void tryChangePassword(View v){
        viewModel.set_email(textEmail.getText().toString().trim());
        viewModel.set_oldPassword(textOldPassword.getText().toString().trim());
        viewModel.set_newPassword01(textNewPassword01.getText().toString().trim());
        viewModel.set_newPassword02(textNewPassword02.getText().toString().trim());

        if(!viewModel.get_email().isEmpty() && !viewModel.get_oldPassword().isEmpty() && !viewModel.get_newPassword01().isEmpty() && !viewModel.get_newPassword02().isEmpty()){//Si se rellenaron todos los campos
            //Get auth credentials from the user for re-authentication.
            AuthCredential credential = EmailAuthProvider.getCredential(viewModel.get_email(), viewModel.get_oldPassword());//Comprobamos si el usuario y la antigua contraseña son correctos

            //Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {//Si el email y la vieja contraseña son correctos
                                if(viewModel.get_newPassword01().equals(viewModel.get_newPassword02())){//Si la repetición de la nueva contraseña es correcta
                                    if(viewModel.get_newPassword01().length() >= ApplicationConstants.FB_MIN_PASSWORD_SIZE){//Si supera el número mínimo de carácteres obligatorios
                                        user.updatePassword(viewModel.get_newPassword01()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {//Se realiza el cambio de contraseña
                                                if (task.isSuccessful()) {//Si se modificó satisfactoriamente
                                                    Toast.makeText(getApplication(), R.string.password_updated, Toast.LENGTH_SHORT).show();
                                                    finish();//Cerramos la actividad actual
                                                } else {
                                                    Toast.makeText(getApplication(), R.string.error_password_updated, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplication(), R.string.password_lenght_error, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplication(), R.string.password_not_equals, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplication(), R.string.login_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, R.string.all_fields_required, Toast.LENGTH_SHORT).show();
        }
    }
}
