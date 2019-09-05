package mtaubert.loginapplication.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users_Table")
data class User(@PrimaryKey @ColumnInfo(name = "email") var email: String,
                @ColumnInfo(name = "password") var password: String,
                @ColumnInfo(name = "name") var name: String
                )