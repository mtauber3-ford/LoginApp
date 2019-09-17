package mtaubert.loginapplication.Features.API.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel

class APIViewModel(app: Application): AndroidViewModel(app) {

    private val apiModel = mtaubert.loginapplication.Features.API.Models.APIModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    /**
     * Returns the current logged in user
     */
    fun getCurrentUser(): User? {
        return apiModel.currentUser
    }

    /**
     * sets the current User
     */
    fun setCurrentUser(currentUser: User) {
        apiModel.currentUser = currentUser
    }
}