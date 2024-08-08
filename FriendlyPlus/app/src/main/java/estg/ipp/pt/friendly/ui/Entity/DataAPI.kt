package estg.ipp.pt.friendly.ui.Entity

import estg.ipp.pt.friendly.ui.Database.LoginModule
import estg.ipp.pt.friendly.ui.Database.Pagamento
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Database.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DataAPI {

    @GET("recintos")
    fun getRecintos(
    ): Call<List<Recintos>>

    @GET("recintos/{recintoID}")
    fun getRecintobyID(@Path("recintoID") recintoID: Int): Call<Recintos>

    @POST("reservas/createreserva")
    fun createReserva(@Body reserva: Reserva): Call<Int>

    @POST("cancelarreserva/{id}")
    fun cancelarReserva(@Path("id") reservaID: Int?): Call<Reserva>

    @POST("user/loginMovel")
    fun login(@Body loginModule: LoginModule): Call<User>

    @POST("user/registoMovel")
    fun registo(@Body user: User): Call<User>

    @POST("pagamento/createpagamentoMovel/{reservaID}/{pontos}")
    fun createPagamento(@Path("reservaID") reservaID: Int, @Path("pontos") pontos: Int, @Body pagamento: Pagamento): Call<Int>


    @GET("reservas/{reservaID}")
    fun getReservabyID(@Path("reservaID") reservaID: Int): Call<Reserva>


    @POST("editar/{id}")
    fun updateUserAPI(@Body user: User, @Path("id") id: Int): Call<User>

}