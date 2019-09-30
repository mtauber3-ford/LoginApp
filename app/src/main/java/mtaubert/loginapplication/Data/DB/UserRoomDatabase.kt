package mtaubert.loginapplication.Data.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mtaubert.loginapplication.Data.DB.DAO.CardConverter
import mtaubert.loginapplication.Data.DB.DAO.FavoritesDAO

import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.DAO.UserDAO
import mtaubert.loginapplication.Data.DB.Model.Favorites

@Database(entities = [User::class, Favorites::class], version = 2)
@TypeConverters(CardConverter::class)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun favDao(): FavoritesDAO

    companion object {
        private var INSTANCE: UserRoomDatabase? = null
        private val USER_DB_NAME = "user_database.db"

        fun getInstance(context: Context): UserRoomDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context.applicationContext, UserRoomDatabase::class.java, USER_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyDB() {
            INSTANCE = null
        }
    }

}

