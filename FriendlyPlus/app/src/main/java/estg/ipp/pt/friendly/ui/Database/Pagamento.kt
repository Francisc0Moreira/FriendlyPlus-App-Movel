package estg.ipp.pt.friendly.ui.Database

data class Pagamento(
    val pagamentoID: Int?,
    val userID: Int,
    val metodoID: Int,
    val total: Double,
)
