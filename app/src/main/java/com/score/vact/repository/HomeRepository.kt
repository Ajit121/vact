package com.score.vact.repository

import com.score.vact.datasource.HomeDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(val homeDataSource: HomeDataSource) {
    suspend fun verifyPhoneNumber(number:String) = homeDataSource.verifyPhoneNumber(number)
    suspend fun verifyOtp(number: String, otp: String) = homeDataSource.verifyOtpNumber(number,otp)
}