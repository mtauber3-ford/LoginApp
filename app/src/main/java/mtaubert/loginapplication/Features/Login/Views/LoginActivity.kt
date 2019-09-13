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
    private var currentFragment = "login"
//    var currentUser: User? = null //Current logged in user, null means no user is logged in

    /**
     * Reload the user details
     */
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        val savedUser = savedInstanceState.getStringArrayList(USER_KEY)
//        if(savedUser != null) {
//            loginModel.currentUser = User(savedUser[1], savedUser[2], savedUser[0])
//        }
//        currentFragment = savedInstanceState.getString(FRAGMENT_KEY, "login")
//        super.onRestoreInstanceState(savedInstanceState)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        changeFragment(currentFragment)
        buildDB()
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        if (loginModel.currentUser != null) {
//            val userData: ArrayList<String>? = arrayListOf(loginModel.currentUser!!.name, loginModel.currentUser!!.email, loginModel.currentUser!!.password)
//            outState.putStringArrayList(USER_KEY, userData)
//        }
//        outState.putString(FRAGMENT_KEY, currentFragment)
//        supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentByTag(currentFragment)!!)
//        super.onSaveInstanceState(outState)
//    }

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
     * Changes the fragment to the desired target fragment
     * Also passes the current login model to the next fragment
     */
    fun changeFragment(target:String, loginModel: LoginModel) {

        this.loginModel.currentUser = loginModel.currentUser

        currentFragment = target
        val fragment = when(target) {
            "admin" -> AdminFragment.newInstance(
                loginModel
            )
//            "login" ->  {
//                loginModel.currentUser = null
//                LoginFragment.newInstance(
//                    loginModel
//                )
//                LoginFragment()
//            }
            "signUp" -> SignUpFragment.newInstance(
                loginModel
            )
            "dashboard" -> AccountFragment.newInstance(
//                loginModel
            )
            "accountDetails" -> AccountDetailsFragment.newInstance(
                loginModel
            )
//            else -> LoginFragment.newInstance(loginModel)
            else -> {
                currentFragment = "login"
                LoginFragment.newInstance()
            }
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

    fun changeFragment(target:String) {
        val fragment = when(target) {
            "dashboard" -> AccountFragment.newInstance()
            "login" -> LoginFragment.newInstance()
            else -> LoginFragment.newInstance()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, fragment, target)
            .commit()
    }
}
