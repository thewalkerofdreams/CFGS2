package com.example.adventuremaps.Activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;
import com.example.adventuremaps.Adapters.ImageAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.ImageGalleryActivityVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageGalleryActivity extends AppCompatActivity {

    private Button btnAddImage, btnDeleteImages;
    private GridView gridView;
    private AlertDialog alertDialogDeleteImages;
    private ImageGalleryActivityVM viewModel;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference("Localizations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_localization_point);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(ImageGalleryActivityVM.class);
        viewModel.set_actualEmailUser(getIntent().getStringExtra("ActualEmailUser"));
        viewModel.set_actualLocalizationPointId(getIntent().getStringExtra("ActualLocalizationPointId"));

        //Instanciamos los elementos de la UI
        gridView = findViewById(R.id.GridViewGalleryImageActivity);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay ninguna imagen seleccionada
                    //TODO
                }else{
                    if(viewModel.get_imagesSelected().contains(item)){//Si la imagen ya estaba seleccionada, la deselecciona
                        viewModel.get_imagesSelected().remove(item);//Eliminamos esa imagen de la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.WhiteItem));//Cambiamos el color de la imagen deseleccionada
                    }else{
                        viewModel.get_imagesSelected().add(item);//Añadimos la imagen a la lista de seleccionadas
                        view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la imagen seleccionada
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(viewModel.get_imagesSelected().isEmpty()){//Si aún no existe ninguna imagen seleccionada
                    ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                    viewModel.get_imagesSelected().add(item);//Añadimos la imagen a la lista de seleccionadas
                    view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la imagen seleccionada
                }

                return true;
            }
        });

        /*viewModel.get_imagesToLoad().add(new ClsImageWithId(BitmapFactory.decodeResource(getResources(), R.drawable.isla)));//TODO Pruebas luego lo sacaremos de FireBase
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.save);
        for(int i = 0 ; i < 100; i++){
            viewModel.get_imagesToLoad().add(new ClsImageWithId(bitmap));
        }
        loadGallery();//Cargamos la galería de imagenes*/

        btnAddImage = findViewById(R.id.btnAddImagesActivityImageGallery);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    deleteImagesDialog();
                }
            }
        });

        if(savedInstanceState != null && viewModel.is_dialogDeleteImagesShowing()) {//Si el dialogo de eliminación estaba abierto lo recargamos
            deleteImagesDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {//Si el usuario seleccionó una imagen de la galería
                //try {
                    final Uri imageUri = data.getData();//Pasaremos la imagen a Bitmap
                    //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    insertImageToFireBase(imageUri);//Almacenamos la imagen en FireBase

                    Bitmap selectedImage = null;
                    try {

                        selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver() , imageUri);

                    }
                    catch (Exception e) {
                        //handle exception
                    }
                    viewModel.get_imagesToLoad().add(new ClsImageWithId(imageUri));//Almacenamos la imagen en el VM
                    loadGallery();//Volvemos a cargar la galería
                /*} catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                }*/

            } else {
                Toast.makeText(this, R.string.you_havent_picked_image, Toast.LENGTH_LONG).show();
            }
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
        ImageAdapter adapter = new ImageAdapter(this, viewModel.get_imagesToLoad()){
            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                View view = super.getView(position, convertView, viewGroup);

                if(viewModel.get_imagesSelected().contains(viewModel.get_imagesToLoad().get(position))){//Si la ruta se encuentra en la lista de seleccionadas
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
     * Comentario: Este método muestra un dialogo por pantalla para eliminar las imagenes seleccionadas del
     * punto de localización actual.
     * Si el usuario confirma la eliminación, se eliminará las imagenes de la plataforma FireBase, en caso
     * contrario no sucederá nada.
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
                //Eliminamos las rutas seleccionadas
                for(int i = 0; i < viewModel.get_imagesSelected().size(); i++){
                    viewModel.get_imagesToLoad().remove(viewModel.get_imagesSelected().get(i));
                }

                viewModel.set_dialogDeleteImagesShowing(false);//Indicamos que el dialogo ha finalizado
                viewModel.get_imagesSelected().clear();//Vaciamos la lista de selecionadas
                loadGallery();//Recargamos la lista de rutas
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
    public void onSaveInstanceState(Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
        super.onSaveInstanceState(outState);

        if(alertDialogDeleteImages != null && alertDialogDeleteImages.isShowing()) {//Si se encuentra abierto el dialogo de eliminación
            alertDialogDeleteImages.dismiss();// close dialog to prevent leaked window
            viewModel.set_dialogDeleteImagesShowing(true);
        }
    }

    /**
     * Interfaz
     * Nombre: insertImageToFireBase
     * Comentario: Este método nos permite subir una imagen a la plataforma FireBase.
     * Cabecera: public void insertImageToFireBase(Uri image)
     * Entrada:
     *  -Uri image
     * Postcondiciones: El método sube una imagen a la plataforma FireBase.
     */
    public void insertImageToFireBase(Uri image){
        final StorageReference riversRef = mStorageRef.child("Images").child(viewModel.get_actualLocalizationPointId()).child(viewModel.get_actualEmailUser()).child(System.currentTimeMillis()+""+getExtension(image));

        riversRef.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        String imageId = localizationReference.push().getKey();
                                        //Insertamos la dirección de la imagen en la base de datos
                                        localizationReference.child(viewModel.get_actualLocalizationPointId()).child("gmailImages").child(viewModel.get_actualEmailUser().replaceAll("[.]", "")).child("LocalizationImages")
                                                .child(imageId).setValue(imageUrl);
                                    }
                                });
                            }
                        }

                        Toast.makeText(getApplication(), R.string.image_uploaded, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplication(), R.string.error_upload_image, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Interfaz
     * Nombre: getExtension
     * Comentario: Este método nos permite obtener la extensión de una dirección Uri.
     * Cabecera: public String getExtension(Uri uri)
     * Entrada:
     *  -Uri uri
     * Postcondiciones: El método devuelve la extensión de la dirección uri asociada al nombre.
     */
    public String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewModel.get_imagesToLoad().clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ClsLocalizationPoint localizationPoint = datas.getValue(ClsLocalizationPoint.class);
                    if (localizationPoint.getLocalizationPointId().equals(viewModel.get_actualLocalizationPointId())) {

                        for (DataSnapshot userEmailImages : datas.child("gmailImages").getChildren()) {
                            for(DataSnapshot images: userEmailImages.child("LocalizationImages").getChildren()){

                                    String imageAddress = String.valueOf(images.getValue());
                                    Uri myUri = Uri.parse(imageAddress);
                                    //final InputStream imageStream = getContentResolver().openInputStream(myUri);
                                    //final Bitmap image = BitmapFactory.decodeStream(imageStream);
                                    Bitmap image = null;
                                    try {

                                        image = MediaStore.Images.Media.getBitmap(getContentResolver() , myUri);

                                    }
                                    catch (Exception e) {
                                        //handle exception
                                    }
                                    /*try{
                                        image = BitmapFactory.decodeStream(getContentResolver().openInputStream(myUri));
                                    }catch (FileNotFoundException e){
                                        e.printStackTrace();
                                    }*/

                                    //TODO Aquí cargamos las uir de string en una lista del VM
                                    viewModel.get_imagesToLoad().add(new ClsImageWithId(myUri));

                            }
                        }

                        loadGallery();

                        break;//TODO No me puedo creer que lo este solucionando así
                    }
                }
                loadGallery();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
