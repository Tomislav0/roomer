package com.tomislav0.roomer.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomislav0.roomer.dataAccess.SignInResponse
import com.tomislav0.roomer.dataAccess.UserRepository
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserRepository
): ViewModel() {
    var addUserResponse by mutableStateOf<SignInResponse>(Response.Success(false))
        private set

    fun addUser(user: User) = viewModelScope.launch {
        addUserResponse = Response.Loading
        addUserResponse = repo.addUser(user)
    }
}