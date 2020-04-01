package com.example.adventuremaps.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.Adapters.ImageAdapter;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.CreateLocalizationPointImageGalleryActivityVM;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateLocalizationPointImageGalleryActivity extends AppCompatActivity {

    private Button btnAddImage, btnDeleteImages;
    private GridView gridView;
    private AlertDialog alertDialogDeleteImages;
    private CreateLocalizationPointImageGalleryActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_localization_point);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(CreateLocalizationPointImageGalleryActivityVM.class);
        viewModel.set_imagesToSave((ArrayList<ClsImageWithId>) getIntent().getSerializableExtra("ImagesToSave"));//Ontenemos las imagenes que se seleccionaron anteriormente

        //Instanciamos los elementos de la UI
        gridView = findViewById(R.id.GridViewGalleryImageActivity);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay ninguna imagen seleccionada, lanzamos la actividad ImageGalleryViewPagerActivity
                    Intent intent = new Intent(getApplication(), CreateLocalizationPointImageGalleryViewPagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ImagesToLoad", viewModel.get_imagesToSave());//Pasamos las imagenes a cargar
                    intent.putExtras(bundle);
                    intent.putExtra("PositionImageSelected", position);//Indicamos la posición de la imagen clicada
                    startActivity(intent);
                }else{
                    if(viewModel.get_imagesSelected().contains(item)){//Si la imagen ya estaba seleccionada, la deselecciona
                        viewModel.get_imagesSelected().remove(item);//Eliminamos esa imagen de la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.WhiteItem));//Cambiamos el color de la imagen deseleccionada
                    }else{
                        selectImage(item, view);
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()) {//Si no existe ninguna imagen seleccionada
                    selectImage(item, view);
                }

                return true;
            }
        });

        loadGallery();

        btnAddImage = findViewById(R.id.btnAddImagesActivityImageGallery);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.set_searchingImage(true);//Indicamos en el VM que el usuario está buscando una imagen en la galería
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);//Nos permite obtener una imagen de la galería del teléfono
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
            }
        });

        btnDeleteImages = findViewById(R.id.btnDeleteImagesActivityImageGallery);
        btnDeleteImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay imagenes seleccionadas
                    Toast.makeText(getApplication(), R.string.no_exist_selected_image, Toast.LENGTH_LONG).show();
                }else{
                    deleteImagesDialog();//Lanzamos un dialogo de eliminación
                }
            }
        });

        if(savedInstanceState != null && viewModel.is_dialogDeleteImagesShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            deleteImagesDialog();
        }
    }

    /**
     * Interfaz
     * Nombre: selectImage
     * Comentario: Este método nos permite seleccionar una imagen de la galería.
     * Cabecera: private void selectImage(ClsImageWithId image, View view)
     * Entrada:
     *  -ClsImageWithId image
     *  -View view
     * Postcondiciones: El método selecciona la imagen de la galería, almacenando la imagen en la lista
     * de seleccionadas y cambiando el color de fondo del item.
     */
    private void selectImage(ClsImageWithId image, View view){
        viewModel.get_imagesSelected().add(image);//Añadimos la imagen a la lista de seleccionadas
        view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la imagen seleccionada
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {//Si el usuario seleccionó una imagen de la galería
                final Uri imageUri = data.getData();
                String imageId = localizationReference.push().getKey();

                viewModel.get_imagesToSave().add(new ClsImageWithId(imageUri.toString(), viewModel.get_actualEmailUser(), imageId));//Almacenamos la imagen en la lisat del VM
                loadGallery();//Volvemos a cargar la galería
            } else {
                Toast.makeText(this, R.string.you_havent_picked_image, Toast.LENGTH_LONG).show();
            }
            viewModel.set_searchingImage(false);
        }
    }

    /**
     * Interfaz
     * Nombre: loadGallery
     * Comentario: Este método nos permite cargar la galería de imagenes.
     * Cabecera: public void loadGallery()
     * Postcondiciones: El método carga la galería de imagenes.
     */
    public void loadGallery(){
        ImageAdapter adapter = new ImageAdapter(this, viewModel.get_imagesToSave()){
            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                View view = super.getView(position, convertView, viewGroup);

                if(viewModel.get_imagesSelected().contains(viewModel.get_imagesToSave().get(position))){//Si la ruta se encuentra en la lista de seleccionadas
                    view.setBackgroundResource(R.color.BlueItem);
                }else{
                    view.setBackgroundResource(R.color.WhiteItem);
                }

                return view;
            }
        };
        gridView.setAdapter(adapter);
    }

    /**
     * Interfaz
     * Nombre: deleteImagesDialog
     * Comentario: Este método muestra un dialogo por pantalla para eliminar las imagenes seleccionadas
     * de la galería actual.
     * Cabecera: public void deleteImagesDialog()
     * Postcondiciones: El método muestra un dialogo por pantalla, si el usuario lo confirma eliminará
     * las imagenes seleccionadas, en caso contrario no sucederá nada.
     */
    public void deleteImagesDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.confirm_delete);// Setting Alert Dialog Title
        alertDialogBuilder.setMessage(R.string.question_delete_images);// Setting Alert Dialog Message
        alertDialogBuilder.setCancelable(false);//Para que no podamos quitar el dialogo sin contestarlo

        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplication(), R.string.images_deleted, Toast.LENGTH_SHORT).show();
                //Eliminamos las imagenes seleccionadas
                for(int i = 0; i < viewModel.get_imagesSelected().size(); i++){
                    viewModel.get_imagesToSave().remove(viewModel.get_imagesSelected().get(i));
                }

                viewModel.set_dialogDeleteImagesShowing(false);//Indicamos que el dialogo ha finalizado
                viewModel.get_imagesSelected().clear();//Vaciamos la lista de imagenes selecionadas
                loadGallery();//Recargamos la galería
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.set_dialogDeleteImagesShowing(false);
            }
        });

        alertDialogDeleteImages = alertDialogBuilder.create();
        alertDialogDeleteImages.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("ImagesToSave", viewModel.get_imagesToSave());
        setResult(3, intent);//Enviamos el resultado antes de llamar al método onBackPressed de la clase padre

        super.onBackPressed();

        finish();//finishing activity
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteImages != null && alertDialogDeleteImages.isShowing()) {//Si se encuentra abierto el dialogo de eliminación
            alertDialogDeleteImages.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteImagesShowing(true);
        }
    }
}