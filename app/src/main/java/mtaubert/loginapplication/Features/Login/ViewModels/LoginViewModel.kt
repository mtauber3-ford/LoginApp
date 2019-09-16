package mtaubert.loginapplication.Features.Login.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.async
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val loginModel = LoginModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    fun getCurrentUser(): User? {
        return loginModel.currentUser
    }

    suspend fun validateUserLoginDetails(email:String, password:String):Boolean {
        var newUser: User = User("test@test.com", "pass", "Test Users")
        loginModel.currentUser = newUser

        val userList = db.userDao().getUser(email)

        if(userList.isNotEmpty() && password == userList[0].password) {
            newUser = User(userList[0].email, userList[0].password, userList[0].name)
            loginModel.currentUser = newUser
            return true
        }

        return false
    }

    suspend fun insertNewUserDetails(user: User): Boolean {
        db.userDao().insert(user)
        return true
    }

    fun logoutCurrentUser() {
        loginModel.currentUser = null
    }
}