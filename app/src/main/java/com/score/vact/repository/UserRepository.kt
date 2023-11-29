/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.score.vact.repository

import android.accounts.NetworkErrorException
import android.util.Log
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import com.score.vact.api.AppService
import com.score.vact.model.UserData
import com.score.vact.vo.ResponseException
import org.json.JSONObject
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class UserRepository @Inject constructor(
    private val appSe: AppService,
    private val sharedPrefs: SharedPrefs
) {
    private val TAG = javaClass.simpleName


    suspend fun login(userName: String, password: String): Resource<UserData> {
        Log.d("Repo ", "api called");
        try {
            val loginResponse = appSe.loginAsync(userName, password).await()

            Log.d(TAG, "response $loginResponse")
            val dataObj = JSONObject(loginResponse)

            if (dataObj.getInt("Output") == 1) {

                val id: Int = dataObj.getInt("Id")
                val name = dataObj.getString("Name")
                val image = dataObj.getString("Image")
                val type = dataObj.getString("Type")
                val isAdmin = dataObj.getInt("IsAdmin")
                val userData = UserData(id = id, name = name, image = image, type = type,isAdmin = isAdmin)
                sharedPrefs.userId = userData.id
                return Resource(
                    status = Status.SUCCESS,
                    data = userData,
                    message = ""
                )
            } else {
                return Resource(
                    status = Status.ERROR,
                    data = null,
                    message = dataObj.getString("Message")
                )
            }
        } catch (ex: NetworkErrorException) {
            ex.printStackTrace()
            return postError("Connectivity exception")
        } catch (ex: UnknownHostException) {
            ex.printStackTrace()
            return postError("Connectivity exception")
        } catch (ex: ResponseException) {
            ex.errorMessage
            return postError(message = ex.errorMessage)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return postError(message = "Something went wrong")
        }

    }
    private  fun postError(message: String): Resource<UserData> {
        return Resource(status = Status.ERROR, data = null, message = message)
    }
}
