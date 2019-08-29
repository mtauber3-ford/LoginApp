package mtaubert.loginapplication.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.DataAccessObjects.UserDAO

class UserRepository(private val userDao: UserDAO) {

    val allWords: LiveData<List<User>> = userDao.getAllUsers()

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}