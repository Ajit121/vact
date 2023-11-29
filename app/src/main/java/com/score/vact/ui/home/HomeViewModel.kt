package com.score.vact.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score.vact.repository.HomeRepository
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(val homeRepository: HomeRepository) : ViewModel() {
    //Just a dummy Int data type. Not used anywhere
    private val _phoneNumberVerificationResponse = MutableLiveData<Resource<Int>>()
    val phoneNumberVerificationResponse: LiveData<Resource<Int>>
        get() = _phoneNumberVerificationResponse

    //Int is the userId coming from API.
    //0 if not exists else there will be a visitorId
    private val _otpVerificationResponse = MutableLiveData<Resource<Int>>()
    val otpVerificationResponse: LiveData<Resource<Int>>
        get() = _otpVerificationResponse


    fun verifyPhoneNumber(number: String) = viewModelScope.launch {
        _phoneNumberVerificationResponse.postValue(
            Resource(
                status = Status.LOADING,
                data = null,
                message = null
            )
        )
        val response = async {
            homeRepository.verifyPhoneNumber(number)
        }.await()
        _phoneNumberVerificationResponse.postValue(response)

    }

    fun verifyOtp(number: String, otp: String) = viewModelScope.launch {
        _otpVerificationResponse.postValue(
            Resource(
                status = Status.LOADING,
                data = null,
                message = null
            )
        )
        val response = async {
            homeRepository.verifyOtp(number,otp)
        }.await()
        _otpVerificationResponse.postValue(response)
    }

    fun clear(){

        _phoneNumberVerificationResponse.postValue(null)
        _otpVerificationResponse.postValue(null)
    }
}
