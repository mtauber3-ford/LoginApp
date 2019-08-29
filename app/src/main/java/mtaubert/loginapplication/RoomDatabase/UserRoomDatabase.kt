package mtaubert.loginapplication.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.DataAccessObjects.UserDAO

@Database(entities = [User::class], version = 1)
public abstract class UserRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserRoomDatabase::class.java,
                        "User"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}