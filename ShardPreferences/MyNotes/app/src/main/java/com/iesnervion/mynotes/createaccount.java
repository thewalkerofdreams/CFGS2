package com.iesnervion.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class createaccount extends AppCompatActivity {

    private EditText email, password, password2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        email = findViewById(R.id.EditTextMail);
        password = findViewById(R.id.EditTextPasswordCreateCount01);
        password2 = findViewById(R.id.EditTextPasswordCreateCount02);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Intenta crear una cuenta en la base de datos de FireBase.
     * @param v Vista sobre la que se ha pulsado, en este caso el boton de crearCuenta.
     */
    public void createAccount(View v){
        if(comprobarCampos()){//Si los campos son validos intentamos registrar al usuario.
            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), R.string.toastCreateAccount, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.toastErrorCreate, Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }

    /**
     * Comprueba que el campo del email no esta vacio y las contraseñas coincidan.
     * @return True si el email y contraseñas son validos. False en caso contrario.
     */
    private boolean comprobarCampos(){
        boolean ret =  true;

        if(email.getText().toString().isEmpty()){ //Si el email esta vacio
            ret = false;
            Toast.makeText(this, R.string.toastErrorEmail, Toast.LENGTH_SHORT).show();
        }else if(!password2.getText().toString().equals(password.getText().toString())){ //Si las contraseñas no coinciden
            ret = false;
            Toast.makeText(this, R.string.toastErrorPassword, Toast.LENGTH_SHORT).show();
        }else if(password2.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            ret = false;
            Toast.makeText(this, R.string.toastErrorPasswordEmpty, Toast.LENGTH_SHORT).show();
        }

        return ret;
    }
}
