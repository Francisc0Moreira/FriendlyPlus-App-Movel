package estg.ipp.pt.friendly.ui.Database

data class Recintos(
    val recintoID: Int,
    val name: String,
    val concelho: String,
    val latitude: String,
    val longitude: String,
    val modalidade: String,
    val preco: Float,
    val imagem: String,
    val description: String,
    val contacto: String,
)
