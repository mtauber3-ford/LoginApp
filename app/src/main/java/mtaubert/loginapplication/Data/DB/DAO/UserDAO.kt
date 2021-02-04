package mtaubert.loginapplication.Data.DB.DAO

import androidx.room.*
import mtaubert.loginapplication.Data.DB.Model.User

@Dao
interface UserDAO {
    @Query("SELECT * from Users_Table")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM Users_Table WHERE email=:email")
    suspend fun getUser(email: String): List<User>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vararg user: User)

    @Update
    suspend fun updateUser(vararg user: User)

    @Delete
    suspend fun deleteUser(vararg user: User)

    @Query("DELETE FROM Users_Table")
    suspend  fun deleteAllUsers()
}