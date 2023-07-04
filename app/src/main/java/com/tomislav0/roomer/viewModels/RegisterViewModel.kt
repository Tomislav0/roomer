package com.tomislav0.roomer.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomislav0.roomer.dataAccess.AuthRepository
import com.tomislav0.roomer.dataAccess.SendEmailVerificationResponse
import com.tomislav0.roomer.dataAccess.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.tomislav0.roomer.models.Response.Success
import com.tomislav0.roomer.models.Response.Loading

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var signUpResponse by mutableStateOf<SignUpResponse>(Success("false"))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(Success(false))
        private set

    fun signUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signUpResponse = Loading
        signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)

    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Loading
        sendEmailVerificationResponse = repo.sendEmailVerification()

    }
}