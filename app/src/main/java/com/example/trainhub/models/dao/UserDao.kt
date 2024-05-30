package com.example.trainhub.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.trainhub.models.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Transaction
    @Query("SELECT * FROM users where id = :id")
    fun getUserById(id: String): User

    @Update
    fun updateUser(user: User)
    @Delete
    fun deleteUser(user: User)

}