package mtaubert.loginapplication.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(@PrimaryKey @ColumnInfo(name = "Email") val email: String, @ColumnInfo(name = "Password") val password: String, @ColumnInfo(name = "Name") val name: String)