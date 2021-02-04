package mtaubert.loginapplication.Features.Login.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import java.lang.Exception

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val loginModel = LoginModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    /**
     * Returns the current logged in user
     */
    fun getCurrentUser(): User? {
        return loginModel.currentUser
    }

    /**
     * sets the current User
     */
    fun setCurrentUser(currentUser: User) {
        loginModel.currentUser = currentUser
    }

    /**
     * Grabs and returns all users from the db
     */
    suspend fun getAllUsers(): List<User> {
        return db.userDao().getAllUsers()
    }

    suspend fun updateUserDetails(updatedUser: User): String? {
        val userDao = db.userDao()

        return if(loginModel.currentUser != null) {
            val users = userDao.getUser(updatedUser.email)

            if(users.isEmpty()) { //User has set a new email
                if(db.userDao().getUser(updatedUser.email).isEmpty()) {
                    userDao.deleteUser(loginModel.currentUser!!) //Get rid of the saved user data for the old email
                    userDao.insert(updatedUser) //Add the user back with the new email
                } else {
                    "The email you're trying to change to already exists!"
                }

            } else { //User just changed password or name
                userDao.updateUser(updatedUser)
            }
            loginModel.currentUser = updatedUser
            null

        } else {
            "An error occurred, please try again!"
        }
    }

    /**
     *
     */
    suspend fun deleteUsersFromDB(selectedUsers: ArrayList<User>): String? {
        return if(selectedUsers.isEmpty()) {
            "No users to delete. Select users if you want to delete them!"
        } else {
            val userDao = db.userDao()
            for (user in selectedUsers) {
                userDao.deleteUser(user)
            }
            null
        }
    }

    /**
     * Checks user login details and returns if they are valid
     */
    suspend fun validateUserLoginDetails(email:String, password:String):Boolean {
        var newUser = User("test@test.com", "pass", "Test Users")
        loginModel.currentUser = newUser

        val userList = db.userDao().getUser(email)

        if(userList.isNotEmpty() && password == userList[0].password) {
            newUser = User(userList[0].email, userList[0].password, userList[0].name)
            loginModel.currentUser = newUser
            return true
        }

        return false
    }

    /**
     * Validates user sign up details and adds them to the db
     */
    suspend fun validateUserSignUpDetails(email: String, password: String, name: String): String? {

        if(name.replace(" ", "") == "") { //Name field was left empty or full of spaces
            return "Your name can not be empty!"
        }

        if(!email.contains("@") || !email.contains(".") || email.contains(" ")) { //Email must contain both @ and . to be a valid email, also cannot contain a space
            return "Your email is not a valid email!"
        }

        if(password.length <= 7) { //Password has to be at least 8  characters
           return "Your password must be at least 8 characters long!"
        }

        if (password.contains(Regex("[^A-Za-z0-9!@#$&]"))) { //Any character outside this set is not valid for passwords
            return "Your password can only contain letters, numbers or these symbols: !@#$&"
        }

        val userDao = db.userDao()

        if(userDao.getUser(email).isNotEmpty()) { //User exists
            return "User already exists, please log in!"
        }

        val user = User(email, password, name)

        return if(insertNewUserDetails(user)) { //User successfully added to the DB
            null
        } else { //Failure when adding user to DB
            "Error creating account, please try again!"
        }
    }

    /**
     * Inserts the new user into the database
     * Signs the user in if everthing goes well
     */
    private suspend fun insertNewUserDetails(user: User): Boolean {
        return try {
            db.userDao().insert(user)
            loginModel.currentUser = user
            true
        } catch (e: Exception) { //any errors caught
            false
        }
    }

    /**
     * logs out the current user
     */
    fun logoutCurrentUser() {
        loginModel.currentUser = null
    }

}