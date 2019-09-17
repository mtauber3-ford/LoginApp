package mtaubert.loginapplication.Features.Login.Views

import android.content.Context
import android.os.Bundle
import mtaubert.loginapplication.Utils.Activities.BaseActivity
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Data.DB.UserRoomDatabase

class LoginActivity : BaseActivity() {

    lateinit var db: UserRoomDatabase //Database used for user info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity_main)
        if(savedInstanceState == null) {
            changeFragment("login")
        }
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
     * Change the fragment
     */
    fun changeFragment(target:String) {
        val fragment = when(target) {
            "dashboard" -> Dashboard.newInstance()
            "login" -> LoginFragment.newInstance()
            "accountDetails" -> AccountDetailsFragment.newInstance()
            "signUp" -> SignUpFragment.newInstance()
            "admin" -> AdminFragment.newInstance()
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
