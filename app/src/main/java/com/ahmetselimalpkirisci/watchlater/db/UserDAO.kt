package com.ahmetselimalpkirisci.watchlater.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmetselimalpkirisci.watchlater.model.User
import com.ahmetselimalpkirisci.watchlater.util.Utils

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(owner: User)

    @Update
    suspend fun updateUser(owner: User)

    @Delete
    suspend fun deleteUser(customer: User)

    @Query("SELECT * FROM ${Utils.TABLENAME_USER}")
    fun getAllUsers(): LiveData<List<User>>

    // Add this suspend function
    @Query("SELECT * FROM ${Utils.TABLENAME_USER}")
    suspend fun getAllUsersList(): List<User>
}
