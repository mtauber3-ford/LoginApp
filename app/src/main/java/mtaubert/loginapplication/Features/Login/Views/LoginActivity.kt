package mtaubert.loginapplication.Features.Login.Views

import android.content.Context
import android.os.Bundle
import androidx.room.Room
import mtaubert.loginapplication.Utils.Activities.BaseActivity
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Data.DB.UserRoomDatabase

const val USER_KEY = "user"

class LoginActivity : BaseActivity() {
    lateinit var db: UserRoomDatabase //Database used for user info
    var currentUser: User? = null //Current logged in user, null means no user is logged in

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val savedUser = savedInstanceState?.getStringArrayList(USER_KEY)
        if(savedUser != null) {
            currentUser = User(savedUser[1], savedUser[2], savedUser[0])
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        buildDB()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        if (currentUser != null) {
            val userData: ArrayList<String>? = arrayListOf(currentUser!!.name, currentUser!!.email, currentUser!!.password)
            outState.putStringArrayList(USER_KEY, userData)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Switches theme between light and dark mode
     */
    fun toggleTheme() {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        when(preferences.getString("current_theme", "DarkTheme")) {
            "LightTheme" -> {
                preferences.edit().putString("current_theme", "DarkTheme").apply()
            }
            "DarkTheme" -> {
                preferences.edit().putString("current_theme", "LightTheme").apply()
            }
        }
        recreate()
    }

    /**
     * Builds the DB
     */
    private fun buildDB() {
        db = Room.databaseBuilder(
            applicationContext,
            UserRoomDatabase::class.java, "user_database.db"
        ).build()
    }
}
