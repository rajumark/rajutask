package com.hexflake.rajutaskevince.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hexflake.rajutaskevince.database.UserModel
import com.hexflake.rajutaskevince.repository.UserListRepo
import com.hexflake.rajutaskevince.utils.exceptionCoroutine

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MainViewModel @ExperimentalPagingApi @Inject constructor(
    private val userListRepo: UserListRepo
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     *
     * we keep track of api calling loading state
     * */
    fun Boolean.refreshLoading() {
        viewModelScope.launch(exceptionCoroutine) {
            run(_isLoading::setValue)
        }
    }

    /**
     * calling first page for the first time
     * */
    fun syncWithServer() {
        getNextPageFromServer(1)
    }


    /**
     * we are getting data from server using page
     * */

    var isDataOver: Boolean = false
    fun getNextPageFromServer(nextPage: Int) {
        if (isDataOver) {
            false.refreshLoading()
            return
        }
        if (_isLoading.value == false) {
            true.refreshLoading()
            viewModelScope.launch(Dispatchers.IO + exceptionCoroutine) {
                val reponse = userListRepo.getUserListFromServer(nextPage)
                val list = reponse.data.orEmpty()
                isDataOver = list.isEmpty()
                list.let { listUsers ->
                    userListRepo.insertUser(listUsers.map { it.copy(page = reponse.page) })
                }.also {
                    false.refreshLoading()
                }
            }
        }
    }

    fun deleteUserFromRoomDB(model: UserModel, afterDeleteted: () -> Unit) {
        viewModelScope.launch(exceptionCoroutine) {
            userListRepo.deleteUserFromRoom(model)
            runBlocking {
                afterDeleteted()
            }
        }
    }
    fun deleteUserFromServer(model: UserModel) {
        viewModelScope.launch(exceptionCoroutine) {
            userListRepo.deleteUserFromServer(model)
        }
    }



    /**
     * single source of truth for list
     * */
    val pagerUserList = Pager(PagingConfig(20)) {
        userListRepo.getUserList()
    }.flow


}


