package jortega.proyecto.gestionnotaria.red;

import java.util.List;

import jortega.proyecto.gestionnotaria.modelos.Cita;
import jortega.proyecto.gestionnotaria.modelos.Despacho;
import jortega.proyecto.gestionnotaria.modelos.LoginResponse;
import jortega.proyecto.gestionnotaria.modelos.LoginRequest;
import jortega.proyecto.gestionnotaria.modelos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("register")
    Call<Void> register(@Body Usuario usuario);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("notarios")
    Call<List<Usuario>> getNotarios();

    @GET("clientes")
    Call<List<Usuario>> getClientes();

    @GET("despachos")
    Call<List<Despacho>> getDespachos();

    @GET("citas")
    Call<List<Cita>> getCitas();

    @POST("citas")
    Call<Void> addCita(@Body Cita cita);

    @PUT("citas/{id}")
    Call<Void> updateCita(@Path("id") int id, @Body Cita cita);

    @DELETE("citas/{id}")
    Call<Void> deleteCita(@Path("id") int id);

    @GET("citas/{id}")
    Call<Cita> getCita(@Path("id") int id);

    @GET("citas/cliente")
    Call<List<Cita>> getCitasPorCliente(@Query("cliente_id") int clienteId);

    @GET("citas/notario")
    Call<List<Cita>> getCitasPorNotario(@Query("notario_id") int notarioId);

    @POST("despachos")
    Call<Void> addDespacho(@Body Despacho despacho);

    @DELETE("despachos/{id}")
    Call<Void> deleteDespacho(@Path("id") int id);
}
