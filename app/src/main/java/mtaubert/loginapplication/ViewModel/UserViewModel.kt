package mtaubert.loginapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.Repository.UserRepository
import mtaubert.loginapplication.RoomDatabase.UserRoomDatabase

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository
    val allUsers: LiveData<List<User>>

    init {
        val userDao = UserRoomDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allWords
    }

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}