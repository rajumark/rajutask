package com.hexflake.rajutaskevince.repository

import androidx.paging.ExperimentalPagingApi
import com.hexflake.rajutaskevince.database.UserModel
import com.hexflake.rajutaskevince.database.UserModelMain
import com.hexflake.rajutaskevince.database.dao.UserListDao
import com.hexflake.rajutaskevince.network.ApiMethodsList
import javax.inject.Inject

@ExperimentalPagingApi
class UserListRepo @Inject constructor(
    private val userListDao: UserListDao,
    private val apiMethodsList: ApiMethodsList,
) {

    /**
     * get all record of users in using pafing source
     * */
    fun getUserList() = userListDao.pagingSource()

    /**
     * insert record to dataabase
     * */
    suspend fun insertUser(userList: List<UserModel>) {
        userListDao.insertAll(userList)
    }

    /**
     * get all users list from server database
     * */
    suspend fun getUserListFromServer(nextPage: Int): UserModelMain {
        return apiMethodsList.getUsersList(nextPage)
    }


    /**
     * delete for server database using api
     * */
    suspend fun deleteUserFromServer(model: UserModel) {
        model.id?.let { id ->
            apiMethodsList.deleteUser(model.id)
        }
    }

    /**
     * delete for room database
     * */
    suspend fun deleteUserFromRoom(model: UserModel) {
        userListDao.deleteSingleUser(model)
    }


}

