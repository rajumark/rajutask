package com.hexflake.rajutaskevince.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.hexflake.rajutaskevince.database.UserModel

import kotlinx.coroutines.flow.Flow

@Dao
interface UserListDao {

    /**
     * insert all user record in to database
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<UserModel>)

    /**
     * delete all record from database
     * */
    @Query("DELETE FROM table_users")
    suspend fun deleteAll(): Int

    /**
     * get all record of users in flow stream
     * */
    @Transaction
    @Query("SELECT * FROM table_users")
    fun pagingSource(): PagingSource<Int, UserModel>

    /**
     * get total number or record in database
     * */
    @Query("SELECT COUNT(id) FROM table_users")
    fun count(): Flow<Int>


    /**
     * delete single user from database
     * */
    @Delete
    suspend fun deleteSingleUser(model: UserModel): Int
}