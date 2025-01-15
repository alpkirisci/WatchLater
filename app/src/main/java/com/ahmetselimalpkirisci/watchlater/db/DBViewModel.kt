// DBViewModel.kt
package com.ahmetselimalpkirisci.watchlater.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ahmetselimalpkirisci.watchlater.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBViewModel(application: Application) : AndroidViewModel(application) {
    val readAllDataUser: LiveData<List<User>>
    private val userDAO: UserDAO = UserRoomDatabase.getDatabase(application).userDao()

    init {
        readAllDataUser = userDAO.getAllUsers()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDAO.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDAO.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDAO.deleteUser(user)
        }
    }
}
