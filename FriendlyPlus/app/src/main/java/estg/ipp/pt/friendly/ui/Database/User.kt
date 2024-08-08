package estg.ipp.pt.friendly.ui.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userID: Int?,
    val userContact: Int?,
    var nome: String,
    var email: String,
    val password: String,
    val isAdmin: Boolean,
    var morada: String,
    var nascimento: String,
    var genero: String,
    var telemovel: String,
    var nif: String,
    var pontos: Int,
    val isAccountActivated: Boolean,
)
