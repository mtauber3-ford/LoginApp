package mtaubert.loginapplication.Data.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.DAO.UserDAO

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO


    companion object {
        private var INSTANCE: UserRoomDatabase? = null
        private val USER_DB_NAME = "user_database.db"

        fun getInstance(context: Context): UserRoomDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, UserRoomDatabase::class.java, USER_DB_NAME).build()
            }
            return INSTANCE!!
        }

        fun destroyDB() {
            INSTANCE = null
        }
    }

}

