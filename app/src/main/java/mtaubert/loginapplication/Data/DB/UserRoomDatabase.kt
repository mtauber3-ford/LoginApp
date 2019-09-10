package mtaubert.loginapplication.Data.DB

import androidx.room.Database
import androidx.room.RoomDatabase

import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.DAO.UserDAO

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}

