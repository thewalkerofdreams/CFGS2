package com.example.adventuremaps.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.Adapters.MyPageAdapter;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.example.adventuremaps.R;
import com.example.adventuremaps.ViewModels.ImageGalleryViewPagerActivityVM;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ImageGalleryViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private RatingBar ratingBar, ratingBarGeneral;
    private ImageGalleryViewPagerActivityVM viewModel;
    private DatabaseReference localizationReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.FB_LOCALIZATIONS_ADDRESS);
    private boolean checkedImageToDelete = false;//Centinela que nos permitirá realizar correctamente las eliminaciones de las imagenes con baja valoración
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_view_pager);

        //Instanciamos el VM
        viewModel = ViewModelProviders.of(this).get(ImageGalleryViewPagerActivityVM.class);
        viewModel.set_actualUserEmail(getIntent().getStringExtra(ApplicationConstants.INTENT_ACTUAL_USER_EMAIL));
        viewModel.set_actualLocalizationPoint((ClsLocalizationPoint) getIntent().getSerializableExtra(ApplicationConstants.INTENT_ACTUAL_LOCALIZATION));
        if(viewModel.get_positionSelectedImage() == -1)//Si aún no se obtuvo la posición de la imagen a mostrar
            viewModel.set_positionSelectedImage(getIntent().getIntExtra(ApplicationConstants.INTENT_POSITION_IMAGE_SELECTED,0));

        //Instanciamos los elementos de la UI
        ratingBarGeneral = findViewById(R.id.RatingBarGeneralImage);
        linearLayout = findViewById(R.id.FrameLayoutRatingBar);

        ratingBar = findViewById(R.id.RatingBarImage);
        reloadRatingBar(0);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {//Cuando se pase de una imagen a otra
                viewModel.set_positionSelectedImage(position);//Guardamos la posición de la imagen seleccionada en el VM
                setGeneralRatingOfActualImage();//Obtenemos el nuevo valor del ratingBarGeneral
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//Ajustamos la pantalla, en landscape ajustamos el layout del rating bar
            LinearLayout linearLayout = findViewById(R.id.LinearLayoutRatingBar);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            param.weight = 12;
            linearLayout.setLayoutParams(param);
        }
    }

    /**
     * Interfaz
     * Nombre: changeVisibilityOfRatingBar
     * Comentario: Este método nos permite cambiar la visibilidad del ratingBar de la imagen actual.
     * Cabecera: public void changeVisibilityOfRatingBar(View view)
     * Entrada:
     *  -View view
     * Postcondiciones: El método cambia la visibilidad del ratingBar de la imagen actual, si se encontraba
     * visible la oculta y viceversa.
     */
    public void changeVisibilityOfRatingBar(View view){
        if(linearLayout.getVisibility() == View.VISIBLE){
            linearLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Interfaz
     * Nombre: changeRatingBarToGone
     * Comentario: Este método nos permite ocultar el ratingBar si este se encuentra visible.
     * Cabecera: public void changeRatingBarToGone()
     * Postcondiciones: El método modifica el estado de visibilidad del LinearLayout que contiene el
     * ratingBar a GONE, si este se encontraba como VISIBLE.
     */
    public void changeRatingBarToGone(){
        if(linearLayout.getVisibility() == View.VISIBLE)
            linearLayout.setVisibility(View.GONE);
    }

    /**
     * Interfaz
     * Nombre: loadValoration
     * Comentario: Este método nos permite cargar la valoración, de una imagen si el usuario
     * ya la valoró con anterioridad. Carga el ratingBar del usuario con la nueva valoración
     * obtenida de la plataforma Firebase.
     * Cabecera: public void loadValoration()
     * Postcondiciones: El método carga la valoración de la imagen actual, si esta fue valorada por
     * el usuario.
     */
    public void loadValoration(){
        reloadRatingBar(0);//Recargamos el ratingBar inferior

        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_EMAIL_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_userEmailCreator().replaceAll("[.]", " ")).
                child(ApplicationConstants.FB_LOCALIZATION_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_imageId()).child(ApplicationConstants.FB_LOCALIZATION_VALORATIONS_CHILD).
                child(viewModel.get_actualUserEmail().replaceAll("[.]", " ")).child(ApplicationConstants.FB_LOCALIZATION_VALORATION_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){//Si el usuario ya ha valorado la imagen
                    float floatValue = (float)(double)dataSnapshot.getValue(Double.class);//Obtenemos la valoración del usuario
                    reloadRatingBar(floatValue);//Recargamos el ratingBar inferior
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Interfaz
     * Nombre: valorateImage
     * Comentario: Este método nos permite almacenar el valor de nuestra valoración sobre una imagen en la
     * plataforma FireBase.
     * Cabecera: public void valorateImage(double valoration)
     * Entrada:
     *  -double valoration
     * Postcondiciones: El método almacena la valoración del usuario en la plataforma FireBase.
     */
    public void valorateImage(final double valoration){
        localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_EMAIL_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_userEmailCreator().replaceAll("[.]", " ")).
                child(ApplicationConstants.FB_LOCALIZATION_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_imageId()).child(ApplicationConstants.FB_LOCALIZATION_VALORATIONS_CHILD).
                child(viewModel.get_actualUserEmail().replaceAll("[.]", " ")).child(ApplicationConstants.FB_LOCALIZATION_VALORATION_CHILD).setValue(valoration);
        checkedImageToDelete = false;
    }

    /**
     * Interfaz
     * Nombre: reloadRatingBar
     * Comentario: Este método nos permite cargar de nuevo el ratingBar inferior con una valoración específica.
     * Cabecera: publuc void reloadRatingBar(float newRating)
     * Entrada:
     *  -float newRating
     * Postcondiciones: El método recarga el ratingBar inferior con una nueva valoración.
     */
    public void reloadRatingBar(float newRating){
        ratingBar.setOnRatingBarChangeListener(null);//Si no lo hacemos, se subiría el primer valor por defecto
        ratingBar.setRating(newRating);//Por si el usuario aún no ha valorado la imgen
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                valorateImage((double) rating);//Almacenamos la valoración en la base de datos
            }
        });
    }

    /**
     * Interfaz
     * Nombre: loadViewPager
     * Comentario: Este método nos permite cargar el viewPager que contiene la galerría de imagenes.
     * Cabecera: public void loadViewPager()
     * Postcondiciones: El método carga el viewPager que contiene la galería de imagenes.
     */
    public void loadViewPager(){
        MyPageAdapter myAdminPageAdapter = new MyPageAdapter(this, viewModel.get_imagesToLoad());
        viewPager.setAdapter(myAdminPageAdapter);
        viewPager.setCurrentItem(viewModel.get_positionSelectedImage());//Mostramos la imagen que se seleccionó en la actividad anterior
    }

    /**
     * Interfaz
     * Nombre: setGeneralRatingOfActualImage
     * Comentario: Este método nos permite obtener la valoración general de la imagen actual, almacenandola
     * en el atributo _generalRatingOfActualImag del VM e inserta el nuevo valor en el ratingBarGeneral de
     * la actividad. Dentro de este método se realiza una llamada al método loadValoration y tryToDeleteImageFromFireBase
     * una vez se hayan obtenido los datos de la plataforma, esto es así para mantener la consistencia de
     * los datos obtenidos por la plataforma. Realizamos las llamadas a esta de una en una para evitar errores
     * de llegada (Es decir, se esperaba un dato necesario para la segunda llamada y llega primero los de la segunda llamada).
     * Cabecera: public void setGeneralRatingOfActualImage()
     * Postcondiciones: El método devuelve un float asociado al nombre, que es el rating general
     * de la imagen actual.
     */
    public void setGeneralRatingOfActualImage(){
        if(viewModel.get_positionSelectedImage() < viewModel.get_imagesToLoad().size()){//Si la posición seleccionada es menor que el tamaño de las imagenes de la gelería
            localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_EMAIL_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_userEmailCreator().replaceAll("[.]", " ")).
                    child(ApplicationConstants.FB_LOCALIZATION_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_imageId()).child(ApplicationConstants.FB_LOCALIZATION_VALORATIONS_CHILD).
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int auxCounter = 0;
                            float totalValoration = 0;
                            for(DataSnapshot emails: dataSnapshot.getChildren()){//Obtenemos la valoración de todos los usuarios
                                totalValoration += (float)(double)emails.child(ApplicationConstants.FB_LOCALIZATION_VALORATION_CHILD).getValue(Double.class);
                                auxCounter++;
                            }

                            float generalRating = auxCounter == 0 ? 0: (totalValoration / auxCounter);
                            viewModel.set_generalRatingOfActualImage(generalRating);//Almacenamos la valoración general en el VM
                            viewModel.set_numberOfValorations(auxCounter);//Obtenemos el número de valoraciones, útil para la eliminación de una imagen
                            ratingBarGeneral.setRating(viewModel.get_generalRatingOfActualImage());//Insertamos la valoración en el ratingBarGeneral

                            loadValoration();//Cargamos la valoración dada por el usuario actual, si tiene una
                            if(!checkedImageToDelete)//Si aún no se ha comprobado si se debe eliminar la imagen
                                tryToDeleteImageFromFireBase();//Comprobamos si se debe eliminar la imagen
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }

    /**
     * Interfaz
     * Nombre: tryToDeleteImageFromFireBase
     * Comentario: Este método intentará eliminar una imagen de un punto de localización de la plataforma
     * Firebase. Si la imagen tiene una valoración general de menos de dos estrellas y ya han valarado la imagen
     * más de 10 personas, se eliminará de la base de datos.
     * Cabecera: public void tryToDeleteImageFromFireBase()
     * Postcondiciones: Si la imagen actual tiene una valoración general de menos de dos estrellas y
     * ya han valorado más de 10 usuarios la imagen, el método la eliminará de la plataforma FireBase.
     */
    public void tryToDeleteImageFromFireBase(){
        if(viewModel.get_numberOfValorations() > 2 && (double) viewModel.get_generalRatingOfActualImage() < 2){//Si se cumple las restricciones de eliminación
            localizationReference.child(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).child(ApplicationConstants.FB_EMAIL_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_userEmailCreator().replaceAll("[.]", " ")).
                    child(ApplicationConstants.FB_LOCALIZATION_IMAGES).child(viewModel.get_imagesToLoad().get(viewModel.get_positionSelectedImage()).get_imageId()).removeValue();

            if(!viewModel.get_imagesToLoad().isEmpty()){//Si la lista de imagenes no se encuentra vacía
                //Modificamos la posición de la imagen seleccionada
                viewModel.set_positionSelectedImage(viewModel.get_positionSelectedImage() > 0 ? (viewModel.get_positionSelectedImage() - 1): 0);
                loadViewPager();//Recargamos el ViewPager
                checkedImageToDelete = true;
            }else{
                finish();//Finalizamos la actividad actual
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        localizationReference.orderByChild(ApplicationConstants.FB_LOCALIZATION_POINT_ID).equalTo(viewModel.get_actualLocalizationPoint().getLocalizationPointId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewModel.get_imagesToLoad().clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    for (DataSnapshot userEmailImages : datas.child(ApplicationConstants.FB_EMAIL_IMAGES).getChildren()) {
                        String emailImage = userEmailImages.getKey();
                        for(DataSnapshot images: userEmailImages.child(ApplicationConstants.FB_LOCALIZATION_IMAGES).getChildren()){
                            String imageId = images.getKey();
                            String imageAddress = String.valueOf(images.child(ApplicationConstants.FB_IMAGES_URI_CHILD).getValue());//Obtenemos la dirección de la imagen
                            //Comenzamos a cargar las imagenes con su uri en una lista del VM
                            if(!imageAddress.equals("null"))//Si la dirección no es nula
                                viewModel.get_imagesToLoad().add(new ClsImageWithId(imageAddress, emailImage.replace("[' ']", "."), imageId));
                        }
                    }
                    changeRatingBarToGone();//Ajustamos la imagen
                    setGeneralRatingOfActualImage();//Obtemos la valoración general de la imagen
                    loadViewPager(); //Recargamos la gelería de imagenes
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
