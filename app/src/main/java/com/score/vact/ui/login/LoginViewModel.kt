package com.score.vact.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.score.vact.vo.Resource
import com.score.vact.model.UserData
import com.score.vact.repository.SharedPrefs
import com.score.vact.repository.UserRepository
import com.score.vact.vo.Status
import kotlinx.coroutines.*
import javax.inject.Inject

class LoginViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {
    private val TAG = javaClass.simpleName
    // TODO: Implement the ViewModel

    val username = MutableLiveData("")
    val password = MutableLiveData("")

    val _userResponse =MutableLiveData<Resource<UserData>>()

    val userResponse:LiveData<Resource<UserData>>
    get() = _userResponse

    fun login() = viewModelScope.launch {

        Log.d(TAG, "login called")
        _userResponse.postValue(Resource(status = Status.LOADING,data = null,message = null))
        val userData = userRepository.login(username.value.toString(), password = password.value.toString())

        _userResponse.postValue(userData)

    }

    val validate = MediatorLiveData<Boolean>().apply {
        addSource(username) {
            value = (isFormValid(username.value.toString(), password.value.toString()))
        }
        addSource(password) {
            value = (isFormValid(username.value.toString(), password.value.toString()))
        }
    }


    private fun isFormValid(username: String, password: String): Boolean {
        Log.d(TAG, "isFormValid called $username and $password values")
        return username.isNotEmpty() && password.isNotEmpty()
    }

}
