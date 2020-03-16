package com.example.adventuremaps.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Adapters.TypeLocalizationPointsAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.DetailsLocalizationPointActivityVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsLocalizationPointActivity extends AppCompatActivity {

    private TextView nameLocalizationPoint, descriptionLocalizationPoint;
    private ListView localizationTypesList;
    private Button btnEditLocalizationPoint, btnFavourite;
    private DetailsLocalizationPointActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");//Tomamos eferencia de las Localizaciones
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization_point_details);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(DetailsLocalizationPointActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmailUser"));
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra("ActualLocalization"));

        //Instanciamos los elementos de la UI
        nameLocalizationPoint = findViewById(R.id.TextViewNameLocalizationPointDetailsActivity);
        nameLocalizationPoint.setText(viewModel.get_actualLocalizationPoint().getName());

        descriptionLocalizationPoint = findViewById(R.id.TextViewDescriptionLocalizationPointDetailsActivity);
        descriptionLocalizationPoint.setText(viewModel.get_actualLocalizationPoint().getDescription());
        localizationTypesList = findViewById(R.id.ListViewLocalizationTypes);

        btnEditLocalizationPoint = findViewById(R.id.btnEditLocalizationPoint);
        btnEditLocalizationPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), EditLocalizationPointActivity.class);
                intent.putExtra("ActualEmailUser", viewModel.get_actualEmailUser());
                intent.putExtra("ActualLocalization", viewModel.get_actualLocalizationPoint());
                intent.putStringArrayListExtra("LocalizationTypes", viewModel.get_localizationTypes());
                intent.putStringArrayListExtra("LocationsIdActualUser", viewModel.get_localizationsIdActualUser());
                startActivityForResult(intent, 0);
            }
        });

        btnFavourite = findViewById(R.id.btnFavouriteLocalizationPointDetails);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFavourite.getBackground().getConstantState() == getResources().getDrawable(R.drawable.fill_star).getConstantState()){//Si el punto de localización estaba marcado como favorito
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);
                    userReference.child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("localizationsId").child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).removeValue();
                }else{
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);
                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("localizationsId").child(
                            viewModel.get_actualLocalizationPoint().getLocalizationPointId())
                            .setValue(viewModel.get_actualLocalizationPoint().getLocalizationPointId());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationTypes().clear();
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);
                    if(localizationPoint.getLocalizationPointId().equals(viewModel.get_actualLocalizationPoint().getLocalizationPointId())){

                        for(DataSnapshot types : datas.child("types").getChildren()){
                            viewModel.get_localizationTypes().add(String.valueOf(types.getValue()));
                        }

                        break;//TODO No me puedo creer que lo este solucionando así
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userReference.orderByChild("email").equalTo(viewModel.get_actualEmailUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_localizationsIdActualUser().clear();//Limpiamos la lista de puntos de localización favoritos
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    for(DataSnapshot booksSnapshot : datas.child("localizationsId").getChildren()){
                        String localizationId = booksSnapshot.getValue(String.class);
                        viewModel.get_localizationsIdActualUser().add(localizationId);
                    }
                }
                if(viewModel.get_localizationsIdActualUser().contains(viewModel.get_actualLocalizationPoint().getLocalizationPointId())){//Si el punto de localización estaba en favoritos
                    btnFavourite.setBackgroundResource(R.drawable.fill_star);//Modificamos el icono
                }else{
                    btnFavourite.setBackgroundResource(R.drawable.empty_star);//Modificamos el icono
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de tipos del punto de localización actual.
     * Cabecera: public void loadList()
     * Postcondiciones: El método nos permite cargar los tipos del punto de localización actual en una lista.
     */
    public void loadList(){
        TypeLocalizationPointsAdapter adapter = new TypeLocalizationPointsAdapter(this, R.layout.localization_type_item_list, viewModel.get_localizationTypes());
        localizationTypesList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                nameLocalizationPoint.setText(data.getStringExtra("NameUpdated"));
                descriptionLocalizationPoint.setText(data.getStringExtra("DescriptionUpdated"));
            }
        }
    }
}
