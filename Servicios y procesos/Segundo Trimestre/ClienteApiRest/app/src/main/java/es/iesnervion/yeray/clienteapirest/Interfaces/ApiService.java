package es.iesnervion.yeray.clienteapirest.Interfaces;

import java.util.List;

import es.iesnervion.yeray.clienteapirest.Entities.Book;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/libro/{id}")
    Call<List<Book>> getBook();
}
