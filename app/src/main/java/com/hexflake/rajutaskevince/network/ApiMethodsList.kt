package com.hexflake.rajutaskevince.network

import com.hexflake.rajutaskevince.database.UserModel
import com.hexflake.rajutaskevince.database.UserModelMain
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiMethodsList {


    /**
     * end point of api for perform get users data from server with pagination
     * */
    @GET("api/users?")
    suspend fun getUsersList(@Query("page") page: Int): UserModelMain

    /**
     * end point of api for perform delete operation on server
     * */
    @DELETE("api/users/{user_id}")
    suspend fun deleteUser(@Query("user_id") user_id: Int): UserModelMain

    /**
     * api function for update user data to server
     * */
    @PUT("api/users/{user_id}")
    suspend fun updateUser(@Query("model") model: UserModel): UserModelMain
}