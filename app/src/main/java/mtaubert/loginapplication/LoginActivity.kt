package mtaubert.loginapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.RoomDatabase.UserRoomDatabase

class LoginActivity : AppCompatActivity() {
    //lateinit var db:DBHelper
    lateinit var db:UserRoomDatabase
    lateinit var data:List<User>
    var currentUser:User? = null //Current logged in user, null means no user is logged in


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                applicationContext,
                UserRoomDatabase::class.java, "user_database.db"
        ).build()

        GlobalScope.launch {
            data = db.userDao().getAllUsers()
        }

    }

}
