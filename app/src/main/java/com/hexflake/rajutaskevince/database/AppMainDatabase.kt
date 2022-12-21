package com.hexflake.rajutaskevince.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.hexflake.rajutaskevince.database.dao.UserListDao



/**
* main room
* */
@Database(entities = [UserModel::class], version = 1, exportSchema = true)
abstract class AppMainDatabase : RoomDatabase() {
    abstract fun userListDao():UserListDao

}