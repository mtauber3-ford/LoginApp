package mtaubert.loginapplication.Features.Login.ViewModels

import androidx.lifecycle.ViewModel
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel

class LoginViewModel: ViewModel() {
    private val loginModel = LoginModel(null) //model for the login details
    private lateinit var db: UserRoomDatabase //Database used for user info

    fun ViewModel(model: LoginModel) {
        //loginModel = model
    }

    fun getCurrentUser(): User? {
        return loginModel.currentUser
    }

    fun validateUserLoginDetails(username:String, password:String):Boolean {
        val newUser = User("test@email.com", "password", "test name")
        loginModel.currentUser = newUser
        return true
    }

    fun logoutCurrentUser() {
        loginModel.currentUser = null
    }
}