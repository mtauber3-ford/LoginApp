package mtaubert.loginapplication.DataAccessObjects

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import mtaubert.loginapplication.Data.User

interface UserDAO {
    @Query("SELECT * from User")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    suspend fun insert(user: User)

    @Query("DELETE FROM User")
    fun deleteAllUsers()
}