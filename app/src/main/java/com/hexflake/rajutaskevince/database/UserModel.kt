package com.hexflake.rajutaskevince.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class UserModelMain(
    val page: Int?=null,
    val per_page: Int?=null,
    val total: Int?=null,
    val total_pages: Int?=null,
    val data: List<UserModel>?=null,
)

@Entity(tableName = "table_users")
data class UserModel(
    @PrimaryKey val id: Int?=null,
    val page: Int?=null,
    val email: String?=null,
    val first_name: String?=null,
    val last_name: String?=null,
    val avatar: String?=null,
):java.io.Serializable {

}

