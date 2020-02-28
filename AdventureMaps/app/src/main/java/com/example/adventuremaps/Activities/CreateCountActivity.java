package com.example.adventuremaps.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.FireBaseEntities.ClsUser;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.CreateCountActivityVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateCountActivity extends AppCompatActivity {

    private EditText textNickName, textEmail, textPassword01, textPassword02;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userReference;
    private CreateCountActivityVM viewModel;
    private ProgressDialog progressDialog;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_count);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(CreateCountActivityVM.class);

        //Inicializamos el objeto FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Instanciamos los Views
        textNickName = findViewById(R.id.EditTextNickNameActivityCreateCount);
        textEmail = findViewById(R.id.EditTextEmailActivityCreateCount);
        textPassword01 = findViewById(R.id.EditTextPasswor01dActivityCreateCount);
        textPassword02 = findViewById(R.id.EditTextPasswor02dActivityCreateCount);
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Performing online registration");
    }

    /**
     * Interfaz
     * Nombre: trySaveCount
     * Comentario: Este método intentará guardar una nueva cuenta en la plataforma FireBase anlazada
     * a esta aplicación. Si el correo a insertar no está aún almacenado en la plataforma y si la contraseña
     * es válida, el método creará y almacenará la nueva cuenta. En caso de error el método mostrará
     * un mensaje de error por pantalla indicando lo sucedido.
     * Cabecera: public void trySaveCount(View v)
     * Entrada:
     *  -View v
     * Postcondiciones: El método crea y almacena una nueva cuenta en la plataforma de FireBase o muestra
     * un mensaje de error por pantalla si el correo ya existe o si la contraseña no es válida.
     */
    public void trySaveCount(View v){
        viewModel.set_nickName(textNickName.getText().toString().trim());
        viewModel.set_email(textEmail.getText().toString().trim());
        viewModel.set_password01(textPassword01.getText().toString().trim());
        viewModel.set_password02(textPassword02.getText().toString().trim());

        if(!viewModel.get_nickName().isEmpty() && !viewModel.get_email().isEmpty() && !viewModel.get_password01().isEmpty() && !viewModel.get_password02().isEmpty()){
            if(viewModel.get_password01().equals(viewModel.get_password02())){
                firebaseAuth.createUserWithEmailAndPassword(viewModel.get_email(), viewModel.get_password01())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.show();
                                //Verificamos que se pudo registrar el usuario
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplication(), R.string.create_count_successful, Toast.LENGTH_SHORT).show();
                                    userReference = FirebaseDatabase.getInstance().getReference("ClsUser");
                                    String userId = userReference.push().getKey();//Obtenemos una id para el usuario
                                    //Almacenamos al nuevo usuario
                                    ClsUser nuevoUsuario = new ClsUser(userId, viewModel.get_nickName(), viewModel.get_email(), viewModel.get_password01());
                                    FirebaseDatabase.getInstance().getReference("Users").
                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(nuevoUsuario);
                                }else{
                                    //Si el correo ya se encontraba registrado en la plataforma, es decir, si ocurre una colisión
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        Toast.makeText(getApplication(), R.string.create_count_error_colision, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplication(), R.string.create_count_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        });
                }else{
                    Toast.makeText(this, R.string.password_not_equals, Toast.LENGTH_SHORT).show();
                }
        }else{
            Toast.makeText(this, R.string.all_fields_required, Toast.LENGTH_SHORT).show();
        }
    }
}