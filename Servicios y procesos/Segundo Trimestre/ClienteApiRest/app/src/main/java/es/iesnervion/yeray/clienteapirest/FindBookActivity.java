package es.iesnervion.yeray.clienteapirest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import es.iesnervion.yeray.clienteapirest.Entities.Book;
import es.iesnervion.yeray.clienteapirest.Lists.AdapterBookList;
import es.iesnervion.yeray.clienteapirest.ViewModels.FindBookActivityVM;

public class FindBookActivity extends AppCompatActivity {

    EditText nameBook;
    AdapterBookList adapter;
    ListView listView;
    FindBookActivityVM findBookActivityVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book);

        nameBook = findViewById(R.id.EditTextNameBook);
        listView = findViewById(R.id.BookList);
        findBookActivityVM = ViewModelProviders.of(this).get(FindBookActivityVM.class);

        //Observer
        final Observer<ArrayList<Book>> bookListObserver = new Observer<ArrayList<Book>>() {
            @Override
            public void onChanged(ArrayList<Book> books) {
                reloadList();
            }
        };

        //Observamos el livedata
        findBookActivityVM.getBookList().observe(this, bookListObserver);
    }

    /*
     * Intefaz
     * Nombre: getBook
     * Comentario: Este método nos permite modificar el estado de la lista de libros
     * del ViewModel FindBookActivityVM.
     * Cabecera: public void getBook(View v)
     * Entrada:
     *   -View v
     * Postcondiciones: El método modifica el estado de la propiedad bookList del FindBookActivityVM.
     * */
    public void getBook(View v){
        findBookActivityVM.getBooksFromApiByName("ssgggg");
    }

    /*
     * Interfaz
     * Nombre: reloadList
     * Comentario: Este método nos permite recargar la lista de libros.
     * Cabecera: public void reloadList()
     * Postcondiciones: El método recarga la lista de libros.
     * */
    public void reloadList(){
        adapter = new AdapterBookList(this, findBookActivityVM.getBookList().getValue());
        listView.setAdapter(adapter);
    }
}
