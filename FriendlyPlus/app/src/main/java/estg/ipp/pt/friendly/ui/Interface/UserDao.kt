package estg.ipp.pt.friendly.ui.Interface


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import estg.ipp.pt.friendly.ui.Database.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE userID = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT COUNT(*) FROM user")
    fun getUserCount(): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun getAnyUser(): User?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: estg.ipp.pt.friendly.ui.Database.User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()

}

