package mtaubert.loginapplication.Data.DB.Model

import androidx.room.*
import mtaubert.loginapplication.Data.Remote.Model.Card

@Entity(foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = arrayOf("email"),
    childColumns = arrayOf("userEmail"),
    onDelete = ForeignKey.CASCADE)],
    tableName = "Favorites_Table"
)
data class Favorites(
    @PrimaryKey(autoGenerate = true) val favId: Int,
    @ColumnInfo(name = "userEmail") val userEmail: String,
    @ColumnInfo(name = "favCardId") val favCardId: String,
    @ColumnInfo(name = "cardJson") val cardJson: String
)