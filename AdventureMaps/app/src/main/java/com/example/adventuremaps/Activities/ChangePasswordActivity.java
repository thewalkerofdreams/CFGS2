package com.example.adventuremaps.Activities;

import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.ChangePasswordActivityVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText textEmail, textOldPassword, textNewPassword01, textNewPassword02;
    private DatabaseReference myDataBaseReference = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth myFireBaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ChangePasswordActivityVM viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(ChangePasswordActivityVM.class);

        //Instanciamos los Views
        textEmail = findViewById(R.id.EditTextEmailActivityChangePassword);
        textOldPassword = findViewById(R.id.EditTextOldPasswordActivityChangePassword);
        textNewPassword01 = findViewById(R.id.EditTextNewPasswor01dActivityChangePassword);
        textNewPassword02 = findViewById(R.id.EditTextNewPasswor02dActivityChangePassword);
    }

    /**
     * Interfaz
     * Nombre: changePassword
     * Comentario: Este método intentará cambiar la contraseña de una cuenta, si la cuenta existía
     * anteriormente en la plataforma y si todos los campos se rellenarón adecuadamente, en caso contrario
     * el método mostrará por pantalla mensajes de error.
     * Cabecera: public void changePassword(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método cambia la contraseña de una cuenta o muestra por pantalla un mensaje de
     * error si alguno de los campos no es válido.
     */
    public void changePassword(View v){
        viewModel.set_email(textEmail.getText().toString().trim());
        viewModel.set_oldPassword(textOldPassword.getText().toString().trim());
        viewModel.set_newPassword01(textNewPassword01.getText().toString().trim());
        viewModel.set_newPassword02(textNewPassword02.getText().toString().trim());

        if(!viewModel.get_email().isEmpty() && !viewModel.get_oldPassword().isEmpty() && !viewModel.get_newPassword01().isEmpty() && !viewModel.get_newPassword02().isEmpty()){
            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider
                    .getCredential(viewModel.get_email(), viewModel.get_oldPassword());

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if(viewModel.get_newPassword01().equals(viewModel.get_newPassword02())){
                                    user.updatePassword(viewModel.get_newPassword01()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplication(), R.string.password_updated, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplication(), R.string.error_password_updated, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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

    /**
     * Interfaz
     * Nombre: autentificarCorreoYCuenta
     * Comentario: Este método nos permite verificar si existe una cuenta en la plataforma
     * con un correo y contraseña específica.
     * Cabecera: public boolean autentificarCorreoYCuenta(String email, String password)
     * Entrada:
     *  -String email
     *  -String password
     * Salida:
     *  -boolean exist
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si existe una
     * cuenta en la plataforma con ese correo y contraseña o false en caso contrario.
     */
    /*public boolean autentificarCorreoYCuenta(String email, String password){
        boolean exist = false;

         user = firebaseApp.auth().currentUser;
        Credential credential = firebase.auth.EmailAuthProvider.credential(
                firebase.auth().currentUser.email,
                providedPassword
        );

        // Prompt the user to re-provide their sign-in credentials

        user.reauthenticateAndRetrieveDataWithCredential(credential).then(function() {
            // User re-authenticated.
        }).catch(function(error) {
            // An error happened.
        });

        return exist;
    }*/
}
