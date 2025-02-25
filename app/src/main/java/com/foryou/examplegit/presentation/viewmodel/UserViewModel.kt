package com.foryou.examplegit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    repository: UserRepository
) : ViewModel() {

    val users: Flow<PagingData<User>> = repository.getUsers()
        .cachedIn(viewModelScope)
}