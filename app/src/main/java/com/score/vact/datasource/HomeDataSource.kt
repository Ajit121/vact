package com.score.vact.datasource

import com.score.vact.api.AppService
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

class HomeDataSource @Inject constructor(val appService: AppService) {
    suspend fun verifyPhoneNumber(number: String): Resource<Int> {
        try {
            val apiResponse = appService.verifyPhoneNumber(number).await()
            val responseOBJ = JSONObject(apiResponse)
            if (responseOBJ.getInt("Output") == 1) {
                return Resource(status = Status.SUCCESS, data = 1, message = null)
            } else {
                return Resource(
                    status = Status.ERROR,
                    data = null,
                    message = responseOBJ.getString("Message")
                )
            }
        } catch (ex: Exception) {
            return Resource(
                status = Status.ERROR,
                data = null,
                message = ex.message ?: "Something went wrong"
            )
        }
    }

    suspend fun verifyOtpNumber(number: String, otp: String): Resource<Int> {
        try {
            val apiResponse = appService.verifyOTP(number, otp).await()
            val responseOBJ = JSONObject(apiResponse)
            if (responseOBJ.getInt("Output") == 1) {
                return Resource(
                    status = Status.SUCCESS,
                    data = responseOBJ.getInt("IsExist"),
                    message = null
                )
            } else {
                return Resource(
                    status = Status.ERROR,
                    data = null,
                    message = responseOBJ.getString("Status")
                )
            }
        } catch (ex: Exception) {
            return Resource(
                status = Status.ERROR,
                data = null,
                message = ex.message ?: "Something went wrong"
            )
        }
    }

}