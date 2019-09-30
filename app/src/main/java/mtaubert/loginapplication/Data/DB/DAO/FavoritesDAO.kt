package mtaubert.loginapplication.Data.DB.DAO

import androidx.room.*
import mtaubert.loginapplication.Data.DB.Model.Favorites

@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM Favorites_Table WHERE userEmail=:email")
    suspend fun getUserFavorites(email: String): List<Favorites>

    @Update
    suspend fun updateFavorites(favorite: Favorites)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorites)

    @Query("DELETE FROM Favorites_Table WHERE userEmail=:email AND favCardId=:cardId")
    suspend fun deleteFavorite(email: String, cardId: String)
}