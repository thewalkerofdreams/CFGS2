package com.example.adventuremaps.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsImageWithId;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.InetAddress;

public class ImageGalleryActivity extends AppCompatActivity {

    private Button btnAddImage, btnDeleteImages;
    private GridView gridView;
    private AlertDialog alertDialogDeleteImages;
    private ProgressDialog progressDialog;
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
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra("ActualLocalizationPoint"));

        //Instanciamos los elementos de la UI
        gridView = findViewById(R.id.GridViewGalleryImageActivity);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClsImageWithId item = (ClsImageWithId) parent.getItemAtPosition(position);//Obtenemos el item de la posición clicada
                if(viewModel.get_imagesSelected().isEmpty()){//Si no hay ninguna imagen seleccionada, lanzamos la actividad ImageGalleryViewPagerActivity
                    throwImageGalleryViewPagerActivity(position);//Lanzamos la galería de imágenes inmersiva
                }else{
                    if(viewModel.get_imagesSelected().contains(item)){//Si la imagen ya estaba seleccionada, la deselecciona
                        unselectImage(item, view);//Deseleccionamos la imagen
                    }else{
                        selectImage(item, view);//Seleccionamos la imagen
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

        btnAddImage = findViewById(R.id.btnAddImagesActivityImageGallery);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageIntoGallery();//Permitimos que el usuario pueda seleccionar una imagen almacenada en el dispositivo actual
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
     * Nombre: throwImageGalleryViewPagerActivity
     * Comentario: Este método nos permite lanzar la actividad ImageGalleryViewPagerActivity
     * con los datos necesarios para cargar dentro las imágenes del punto de localización
     * actual. La primera imagen en mostrarse será la que tenga una posición igual al parámetro
     * de entrada "imagePosition".
     * Cabecera: private void throwImageGalleryViewPagerActivity(int imagePosition)
     * Entrada:
     *  -int imagePosition
     * Precondiciones:
     *  -imagePosition debe ser un número entre 0 (que será la primera posición) y el número total de imágenes de la localización (menos el tamaño exacto).
     * Postcondiciones: El método lanza la actividad ImageGalleryViewPagerActivity, con la información
     * necesaria para cargar todas las imágenes de la localización actual, mostrando primero una en específico.
     */
    private void throwImageGalleryViewPagerActivity(int imagePosition){
        Intent intent = new Intent(getApplication(), ImageGalleryViewPagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ImagesToLoad", viewModel.get_imagesToLoad());//Pasamos las imagenes a cargar
        intent.putExtras(bundle);
        intent.putExtra("PositionImageSelected", imagePosition);//Indicamos la posición de la imagen clicada
        intent.putExtra("ActualUserEmail", viewModel.get_actualEmailUser());
        intent.putExtra("ActualLocalization", viewModel.get_actualLocalizationPoint());
        startActivity(intent);
    }

    /**
     * Interfaz
     * Nombre: getImageIntoGallery
     * Comentario: Este método permite que el usuario seleccione una imagen almacenada en el
     * dispositivo actual.
     * Cabecera: private void getImageIntoGallery()
     * Postcondiciones: El método permite que el usuario seleccione una imagen de la galería
     * de imágenes del dispositivo actual.
     */
    private void getImageIntoGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);//Nos permite obtener una imagen de la galería del teléfono
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ApplicationConstants.REQUEST_CODE_UPLOAD_IMAGE_FROM_OWN_GALLERY);
    }

    /**
     * Interfaz
     * Nombre: selectImage
     * Comentario: Este método nos permite seleccionar una imagen de la galería,
     * si se intenta seleccionar una imagen sobre la que el usuario actual no tiene
     * permisos de eliminación, el método muestra un mensaje por pantalla indicandolo.
     * Este método se realizó sobre todo para evitar repetir código.
     * Cabecera: private void selectImage(ClsImageWithId image)
     * Entrada:
     *  -ClsImageWithId image
     * Postcondiciones: El método selecciona la imagen de la galería si el usuario tenía permiso
     * para ello, es decir si la imagen la subió él o es el creador del punto de localización actual,
     * en caso contrario el método muestra un mensaje por pantralla indicandole que no tiene el permiso
     * para eliminarla.
     */
    private void selectImage(ClsImageWithId image, View view){
        if(viewModel.get_actualEmailUser().replaceAll("[.]", " ").equals(image.get_userEmailCreator()) ||
                viewModel.get_actualEmailUser().equals(viewModel.get_actualLocalizationPoint().getEmailCreator())){//Si el usuario tiene permiso para eliminarla
            viewModel.get_imagesSelected().add(image);//Añadimos la imagen a la lista de seleccionadas
            view.setBackgroundColor(getResources().getColor(R.color.BlueItem));//Cambiamos el color de la imagen seleccionada
        }else{
            Toast.makeText(getApplication(), R.string.no_permisission_to_delete_image, Toast.LENGTH_LONG).show();
        }
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
        //Cambiamos el color de la imagen deseleccionada
        if(image.get_userEmailCreator().replaceAll(" ", ".").equals(viewModel.get_actualEmailUser())){//Si la imagen pertence al usuario actual
            view.setBackgroundResource(R.color.colorAccent);
        }else{
            view.setBackgroundResource(R.color.WhiteItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplicationConstants.REQUEST_CODE_UPLOAD_IMAGE_FROM_OWN_GALLERY) {
            if (resultCode == RESULT_OK) {//Si el usuario seleccionó una imagen de la galería
                if(isOnline(this)){//Si el dispositivo tiene conexión a Internet
                    final Uri imageUri = data.getData();
                    insertImageToFireBase(imageUri);//Almacenamos la imagen en FireBase
                    loadGallery();//Volvemos a cargar la galería
                }else{
                    Toast.makeText(this, R.string.error_upload_image_without_connection, Toast.LENGTH_LONG).show();
                }
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
                    if(viewModel.get_imagesToLoad().get(position).get_userEmailCreator().replaceAll(" ", ".").equals(viewModel.get_actualEmailUser())){//Si la imagen pertence al usuario actual
                        view.setBackgroundResource(R.color.colorAccent);
                    }else{
                        view.setBackgroundResource(R.color.WhiteItem);
                    }
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
                deleteSelectedImages();//Eliminamos las imagenes seleccionadas de la galería

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
        DatabaseReference drImages;

        for(int i = 0; i < viewModel.get_imagesSelected().size(); i++){
            drImages = FirebaseDatabase.getInstance().getReference("Localizations").child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child("emailImages").
                    child(viewModel.get_imagesSelected().get(i).get_userEmailCreator().replaceAll("[.]", " ")).child("LocalizationImages").child(viewModel.get_imagesSelected().get(i).get_imageId());
            drImages.removeValue();
        }
        viewModel.get_imagesSelected().clear();//Vaciamos la lista de imagenes selecionadas
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {//Lo utilizamos para permitir que el dialogo sobreviva a los cambios de la pantalla
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
        final StorageReference riversRef = mStorageRef.child("Images").child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(viewModel.get_actualEmailUser()).
                child(System.currentTimeMillis()+""+getExtension(image));//La imagen se colgará con la fecha de subida como nombre y su correspondiente extensión

        progressDialog = new ProgressDialog(this);//Isntanciamos el progressDialog
        showProgressDialogWithTitle("Uploading the image", "Wait a moment please...");

        riversRef.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        hideProgressDialogWithTitle();
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();//Necesitamos transformarla en un String para subirla a la plataforma
                                        String imageId = localizationReference.push().getKey();//Obtenemos una id para la imagen
                                        //Insertamos la dirección de la imagen en la base de datos
                                        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child("emailImages").child(viewModel.get_actualEmailUser().replaceAll("[.]", " ")).child("LocalizationImages")
                                                .child(imageId).child("Uri").setValue(imageUrl);

                                        viewModel.get_imagesToLoad().add(new ClsImageWithId(uri.toString(), viewModel.get_actualEmailUser(), imageId));//Almacenamos la imagen en el VM
                                    }
                                });
                            }
                        }
                        Toast.makeText(getApplication(), R.string.image_uploaded, Toast.LENGTH_SHORT).show();//Indicamos que la imagen se ha subido
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideProgressDialogWithTitle();
                        Toast.makeText(getApplication(), R.string.error_upload_image, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress((int) progress);
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
        localizationReference.orderByChild("localizationPointId").equalTo(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.get_imagesToLoad().clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    for (DataSnapshot userEmailImages : datas.child("emailImages").getChildren()) {
                        String emailImage = userEmailImages.getKey();
                        for(DataSnapshot images: userEmailImages.child("LocalizationImages").getChildren()){
                            String imageId = images.getKey();
                            String imageAddress = String.valueOf(images.child("Uri").getValue());//Obtenemos la dirección de la imagen
                            //Comenzamos a cargar los imagenes con su uri en una lista del VM
                            if(!imageAddress.equals("null"))//Si la dirección no es nula
                                viewModel.get_imagesToLoad().add(new ClsImageWithId(imageAddress, emailImage.replace("[' ']", "."), imageId));
                        }
                    }
                    loadGallery();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Metodos para el progressDialog

    /**
     * Interfaz
     * Nombre: showProgressDialogWithTitle
     * Comentario: Este método nos permite mostrar el dialogo de progreso que hemos instanciado en
     * el método OnCreate de la actividad.
     * Cabecera: private void showProgressDialogWithTitle(String title, String substring)
     * @param title
     * @param substring
     */
    private void showProgressDialogWithTitle(String title,String substring) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(substring);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    /**
     * Interfaz
     * Nombre: hideProgressDialogWithTitle
     * Comentario: Este método nos permite ocultar el dialogo de progreso.
     * Cabecera: private void hideProgressDialogWithTitle()
     * Postcondiciones: El método oculta el dialogo de proceso de la actividad.
     */
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }

    /**
     * Interfaz
     * Nombre: isOnline
     * Comentario: Este método nos permite verificar si el dispositivo actual tiene conexión a Internet.
     * Cabecera: public boolean isOnline(Context context)
     * Entrada:
     *  -Context context
     * Salida:
     *  -boolean connection
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre, true si el dispositivo
     * actual tiene conexión a Internet o false en caso contrario.
     */
    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
