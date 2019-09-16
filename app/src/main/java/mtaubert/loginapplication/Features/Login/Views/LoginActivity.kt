package mtaubert.loginapplication.Features.Login.Views

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import mtaubert.loginapplication.Utils.Activities.BaseActivity
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel

const val USER_KEY = "user"
const val FRAGMENT_KEY = "fragment"

class LoginActivity : BaseActivity() {

    lateinit var loginViewModel: LoginViewModel

    lateinit var db: UserRoomDatabase //Database used for user info
    private val loginModel = LoginModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            changeFragment("login")
        }
        buildDB()
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

    /**
     * Change the fragment
     */
    fun changeFragment(target:String) {
        val fragment = when(target) {
            "dashboard" -> AccountFragment.newInstance()
            "login" -> LoginFragment.newInstance()
            "accountDetails" -> AccountDetailsFragment.newInstance()
            else -> LoginFragment.newInstance()
        }

        if(target != "login") {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, fragment, target)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, fragment, target)
                .commit()
        }
    }
}
