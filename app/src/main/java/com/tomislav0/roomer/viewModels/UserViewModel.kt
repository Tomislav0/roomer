package com.tomislav0.roomer.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.ktx.Firebase
import com.tomislav0.roomer.dataAccess.AuthRepository
import com.tomislav0.roomer.dataAccess.GetUsersResponse
import com.tomislav0.roomer.dataAccess.SignInResponse
import com.tomislav0.roomer.dataAccess.UserRepository
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    var addUserResponse by mutableStateOf<SignInResponse>(Response.Success(false))
        private set

    var getuAllUsersResponse by mutableStateOf<List<User>>(mutableListOf())
        private set

    fun addUser(user: User) = viewModelScope.launch {
        addUserResponse = Response.Loading
        addUserResponse = repo.addUser(user)
    }

    val currentUser: Flow<List<User>>
        get() =
            authRepository.currentUser.flatMapLatest { userId ->
                FirebaseFirestore.getInstance().collection("users").whereEqualTo("id", userId).dataObjects()
            }

     val users: Flow<List<User>>
        get() =
            authRepository.currentUser.flatMapLatest { userId ->
                FirebaseFirestore.getInstance().collection("users").whereNotEqualTo("id", userId).dataObjects()
            }
}