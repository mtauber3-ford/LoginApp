package mtaubert.loginapplication.Features.Login.ViewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val loginModel = LoginModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    fun getCurrentUser(): User? {
        return loginModel.currentUser
    }

    fun validateUserLoginDetails(email:String, password:String):Boolean {
        var newUser: User
        
        GlobalScope.launch(Dispatchers.Main) {
            val userList = db.userDao().getUser(email)
            
            if(userList.isEmpty()) {
                
            } else {
                if(password == userList[0].password) {
                    newUser = User(userList[0].email, userList[0].password, userList[0].name)
                }
            }
        }
        loginModel.currentUser = newUser
        return true
    }

    fun logoutCurrentUser() {
        loginModel.currentUser = null
    }
}