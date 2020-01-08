package es.iesnervion.yeray.clienteapirest.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.clienteapirest.Entities.Book;
import es.iesnervion.yeray.clienteapirest.Interfaces.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindBookActivityVM extends AndroidViewModel {

    private String bookName;
    private MutableLiveData<ArrayList<Book>> bookList;

    public FindBookActivityVM(Application application){
        super(application);
        bookList = new MutableLiveData<ArrayList<Book>>();
    }

    //Get Y Set
    public String getBookName(){
        return bookName;
    }

    public void setBookName(String bookName){
        this.bookName = bookName;
    }

    public MutableLiveData<ArrayList<Book>> getBookList(){
        return this.bookList;
    }

    public void setBookList(ArrayList<Book> bookList){
        this.bookList.setValue(bookList);
    }

    //Métodos Añadidos

    /*
    * Interfaz
    * Nombre: getBooksFromApiByName
    * Comentario: Este método nos permite obtener una lista de libros de nuestro
    * servidor Api Rest, según un nombre específico. El valor de nuestra propiedad
    * bookList pasará a tener los valores de la lista obtenida por la Api.
    * Cabecera: public void getBooksFromApiByName(String name)
    * Entrada:
    *   -String name
    * Postcondiciones: El método modifica el estado del atributo bookList.
    * */
    public void getBooksFromApiByName(String name){
        //TODO Hacer esta parte
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://biblioteca.devel:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Book>> call = apiService.getBook();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList.getValue().clear();//Limpiamos la lista de libros
                for(Book book : response.body()) {
                    bookList.getValue().add(book);//Insertamos todos los libros obtenidos
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {//En caso de fallo no hacemos nada
            }
        });
    }
}
