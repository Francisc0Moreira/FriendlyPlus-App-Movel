package estg.ipp.pt.friendly.ui.Database

    data class Reserva(
        var reservaID: Int? = 0,
        val recintoID: Int = 0,
        val userID: Int = 0,
        val userContactID: Int? = 0,
        val pagamentoID: Int? = 0,
        val dataInicial: String = "",
        val horaReserva: String = "",
        val horaJogo: String = "",
        val horaCancelamento: String? = "",
        val preco: Double = 0.0,
        var estado: String = "",
    )
