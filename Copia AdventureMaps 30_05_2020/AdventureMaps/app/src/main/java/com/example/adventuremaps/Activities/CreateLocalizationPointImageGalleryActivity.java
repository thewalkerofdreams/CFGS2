package com.example.adventuremaps.Activities;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Management.ApplicationConstants;
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
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_localization_point);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(CreateLocalizationPointImageGalleryActivityVM.class);
        viewModel.set_imagesToSave((ArrayList<ClsImageWithId>) getIntent().getSerializableExtra(ApplicationConstants.INTENT_IMAGES_TO_SAVE));//Obtenemos las imágenes que se seleccionaron anteriormente

        //Instanciamos los elementos de la UI
        gridView = findViewById(R.id.GridViewGalleryImageActivity);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay ninguna imagen seleccionada
                    throwCreateLocalizationPointImageGalleryViewPagerActivity(position);//Lanzamos la actividad throwCreateLocalizationPointImageGalleryViewPagerActivity
                }else{
                    if(viewModel.get_imagesSelected().contains(item)){//Si la imagen ya estaba seleccionada, la deselecciona
                        unselectImage(item, view);//Eliminamos la imagen de la lista de seleccionadas
                    }else{
                        selectImage(item, view);//Añadimos la imagen a la lista de seleccionadas
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()) {//Si no existe ninguna imagen seleccionada
                    selectImage(item, view);//Seleccionamos la imagen
                }
                return true;
            }
        });

        loadGallery();//Cargamos la galería de imágenes

        btnAddImage = findViewById(R.id.btnAddImagesActivityImageGallery);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchImageIntoGallery();//El usuario busca una imagen almacenada en su dispositivo
            }
        });

        btnDeleteImages = findViewById(R.id.btnDeleteImagesActivityImageGallery);
        btnDeleteImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay imágenes seleccionadas
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
     * Nombre: throwCreateLocalizationPointImageGalleryViewPagerActivity
     * Comentario: El método lanza la actividad CreateLocalizationPointImageGalleryViewPagerActivity,
     * con la información necesaria para cargar la galería de imagenes expandidas. El método requiere
     * la posición de la primera imagen a mostrar.
     * Cabecera: private void throwCreateLocalizationPointImageGalleryViewPagerActivity(int imagePosition)
     * Entrada:
     *  -int imagePosition
     * Precondiciones:
     *  -imagePosition debe hacer referencia a una imagen existente en la galería
     * Postcondiciones: El método lanza la actividad CreateLocalizationPointImageGalleryViewPagerActivity.
     */
    private void throwCreateLocalizationPointImageGalleryViewPagerActivity(int imagePosition){
        Intent intent = new Intent(getApplication(), CreateLocalizationPointImageGalleryViewPagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ApplicationConstants.INTENT_IMAGES_TO_LOAD, viewModel.get_imagesToSave());//Pasamos las imágenes a cargar
        intent.putExtras(bundle);
        intent.putExtra(ApplicationConstants.INTENT_POSITION_IMAGE_SELECTED, imagePosition);//Indicamos la posición de la imagen clicada
        startActivity(intent);
    }

    /**
     * Interfaz
     * Nombre: searchImageIntoGallery
     * Comentario: Este método nos permite que el usuario busque una imagen almacenada
     * en su dispositivo. Si el usuario selecciona alguna imagen, esta se guardará para
     * la nueva localización.
     * Cabecera: private void searchImageIntoGallery()
     * Postcondiciones: El método permite al usuario buscar una imagen almacenada en el
     * dispositivo actual.
     */
    private void searchImageIntoGallery(){
        viewModel.set_searchingImage(true);//Indicamos en el VM que el usuario está buscando una imagen en la galería
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);//Nos permite obtener una imagen de la galería del teléfono
        photoPickerIntent.setType(ApplicationConstants.PHOTO_PICKER_TYPE);
        startActivityForResult(photoPickerIntent, ApplicationConstants.REQUEST_CODE_UPLOAD_IMAGE_FROM_OWN_GALLERY);
    }

    /**
     * Interfaz
     * Nombre: selectImage
     * Comentario: Este método nos permite seleccionar una imagen de la galería.
     * Cabecera: private void selectImage(ClsImageWithId image, View view)
     * Entrada:
     *  -ClsImageWithId image
     *  -View view
     * Postcondiciones: El método selecciona una imagen de la galería, almacenando la imagen en la lista
     * de seleccionadas y cambiando el color de fondo del item.
     */
    private void selectImage(ClsImageWithId image, View view){
        viewModel.get_imagesSelected().add(image);//Añadimos la imagen a la lista de seleccionadas
        view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la imagen seleccionada
    }

    /**
     * Interfaz
     * Nombre: unselectImage
     * Comentario: Este método nos permite deseleccionar una imagen de la galería.
     * Cabecera: private void selectImage(ClsImageWithId image, View view)
     * Entrada:
     *  -ClsImageWithId image
     *  -View view
     * Postcondiciones: El método deselecciona una imagen de la galería, eliminando la imagen de la lista
     * de seleccionadas del VM y cambiando el color de fondo del item.
     */
    private void unselectImage(ClsImageWithId image, View view){
        viewModel.get_imagesSelected().remove(image);//Eliminamos la imagen de la lista de seleccionadas
        view.setBackgroundColor(getResources().getColor(R.color.WhiteItem));//Cambiamos el color de la imagen deseleccionada
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplicationConstants.REQUEST_CODE_UPLOAD_IMAGE_FROM_OWN_GALLERY) {
            if (resultCode == RESULT_OK && data != null) {//Si el usuario seleccionó una imagen de la galería y se obtuvo datos de respuesta
                Uri imageUri = data.getData();
                String imageId = localizationReference.push().getKey();
                if(imageUri != null && imageId != null){//Si se pudo obtener correctamente la URI de la imagen y su nuevo id
                    viewModel.get_imagesToSave().add(new ClsImageWithId(imageUri.toString(), viewModel.get_actualEmailUser(), imageId));//Almacenamos la imagen en la lista del VM
                    loadGallery();//Volvemos a cargar la galería
                }
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
                deleteSelectedImages();//Eliminamos las imagenes seleccionadas

                viewModel.set_dialogDeleteImagesShowing(false);//Indicamos que el dialogo ha finalizado
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

    /**
     * Interfaz
     * Nombre: deleteSelectedImages
     * Comentario: El método elimina las imagenes seleccionadas de la galería actual.
     * Cabecera: private void deleteSelectedImages()
     * Postcondiciones: El método elimina las imagenes seleccionadas de la galería.
     */
    private void deleteSelectedImages(){
        for(int i = 0; i < viewModel.get_imagesSelected().size(); i++){
            viewModel.get_imagesToSave().remove(viewModel.get_imagesSelected().get(i));
        }
        viewModel.get_imagesSelected().clear();//Vaciamos la lista de imagenes selecionadas
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra(ApplicationConstants.INTENT_IMAGES_TO_SAVE, viewModel.get_imagesToSave());
        setResult(Activity.RESULT_OK, intent);//Enviamos el resultado antes de llamar al método onBackPressed de la clase padre

        super.onBackPressed();

        finish();//finishing activity
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteImages != null && alertDialogDeleteImages.isShowing()) {//Si se encuentra abierto el dialogo de eliminación
            alertDialogDeleteImages.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteImagesShowing(true);
        }
    }
}
