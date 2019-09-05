package mtaubert.loginapplication.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.DataAccessObjects.UserDAO

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}

