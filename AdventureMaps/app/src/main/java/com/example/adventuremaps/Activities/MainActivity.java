package com.example.adventuremaps.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adventuremaps.Activities.AutoRestartApp.MyExceptionHandler;
import com.example.adventuremaps.Activities.ui.MainTabbet.MainTabbetActivity;
import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.MainActivityVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail, textPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private MainActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));//Si la aplicación llegará a crasher por algún casual, la reiniciamos
        if (getIntent().getBooleanExtra(ApplicationConstants.AUTORESTART_APP_CRASH, false)) {
            Toast.makeText(this, R.string.reloading_datas, Toast.LENGTH_SHORT).show();
        }

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(MainActivityVM.class);

        //Inicializamos el objeto FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Instanciamos las views
        textEmail = findViewById(R.id.EditTextNickNameActivityLogin);
        textPassword = findViewById(R.id.EditTextPasswordActivityLogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.performing_online_consultation));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){//Si ya existe una sesión iniciada
            startActivity(new Intent(getApplication(), MainTabbetActivity.class).putExtra(ApplicationConstants.INTENT_LOGIN_EMAIL, viewModel.get_email()));
            finish();
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla, en landscape eliminamos el icono de la aplicación
            //Ajustamos el LinearLayout que contiene la imagen
            LinearLayout linearLayout = findViewById(R.id.LinearLayoutLoginActivity);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, (float) 1.0);
            param.weight = 20;
            param.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams(param);
            //Ajustamos la imagen
            ImageView imageView = findViewById(R.id.ImageViewLoginActivity);
            param = new LinearLayout.LayoutParams(150, 150, (float) 1.0);
            imageView.setLayoutParams(param);
        }
    }

    /**
     * Interfaz
     * Nombre: tryLogUser
     * Comentario: Este método intentará loguear un usuario en la aplicación, en caso de introducir
     * un usuario o contraseña incorrecta, el método mostrará un mensaje por pantalla y no se realizará
     * el logueo.
     * Cabecera: public void tryLogUser(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método loguea un usuario en la aplicación o muestra un mensaje de error por
     * pantalla debido al intentar loguearse con un usuario o contraseña incorrecta.
     */
    public void tryLogUser(View v){
        viewModel.set_email(textEmail.getText().toString().trim());
        viewModel.set_password(textPassword.getText().toString().trim());

        if(!viewModel.get_email().isEmpty()){
            if(!viewModel.get_password().isEmpty()){
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(viewModel.get_email(), viewModel.get_password())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Verificamos que se pudo registrar el usuario
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplication(), R.string.login_successful, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplication(), MainTabbetActivity.class).putExtra(ApplicationConstants.INTENT_LOGIN_EMAIL, viewModel.get_email()));
                                    finish();//Finalizamos la actividad actual
                                }else{
                                    Toast.makeText(getApplication(), R.string.login_error, Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {//Si ocurrió algún error en la conexión con el servidor
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();//Indicamos el fallo
                        }
                });
            }else{
                Toast.makeText(this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interfaz
     * Nombre: throwCreateCountActivity
     * Comentario: Este método lanza la actividad CreateCountActivity.
     * Cabecera: public void throwCreateCountActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad CreateCountActivity.
     */
    public void throwCreateCountActivity(View v){
        startActivity(new Intent(this, CreateCountActivity.class));
    }

    /**
     * Interfaz
     * Nombre: throwSendNewPasswordActivity
     * Comentario: Este método lanza la actividad SendNewPasswordActivity.
     * Cabecera: public void throwSendNewPasswordActivity(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método lanza la actividad SendNewPasswordActivity.
     */
    public void throwSendNewPasswordActivity(View v){
        startActivity(new Intent(this, SendNewPasswordActivity.class));
    }
}
