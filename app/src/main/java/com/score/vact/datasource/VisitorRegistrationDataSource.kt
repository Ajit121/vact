package com.score.vact.datasource

import android.accounts.NetworkErrorException
import android.util.Log
import com.google.gson.Gson
import com.score.vact.api.AppService
import com.score.vact.model.*
import com.score.vact.vo.Resource
import com.score.vact.vo.ResponseException
import com.score.vact.vo.Status
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject


class VisitorRegistrationDataSource @Inject constructor(private val appService: AppService) {
    private val TAG = javaClass.simpleName

    suspend fun getCountries():List<CountryData>{
        var countryList:List<CountryData> = listOf()

        try {
            val countriesResponse = appService.getCountries().await()
            val responseObj = JSONObject(countriesResponse)
            if (responseObj.getInt("Output") == 1) {
                countryList = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<CountryData>::class.java
                ).toList()

                Log.d(TAG, "api countryList ${countryList.size}")

               // registrationFormDao.insertCountries(countries = countryList)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            Log.d(TAG, "returning countryList ")

            return countryList
           // return registrationFormDao.getCountries()
        }
    }

    suspend fun getStates(countryId:String):List<StateData>{
        var stateList:List<StateData> = listOf()
        try{
            val apiResponse = appService.getStates(countryId).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                 stateList = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<StateData>::class.java
                ).toList()


                Log.d(TAG, "api countryList ${stateList.size}")
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }finally {
            return stateList
        }
    }

    suspend fun getDistricts(stateId:String):List<DistrictData>{
        var districtList:List<DistrictData> = listOf()
        try{
            val apiResponse = appService.getDistricts(stateId).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                 districtList = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<DistrictData>::class.java
                ).toList()


                Log.d(TAG, "api countryList ${districtList.size}")
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }finally {
            return districtList
        }
    }

    suspend fun getCities(districtId:String):List<CityData>{
        var cityList:List<CityData> = listOf()
        try{
            val apiResponse = appService.getCities(districtId).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                 cityList = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<CityData>::class.java
                ).toList()


                Log.d(TAG, "api countryList ${cityList.size}")
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }finally {
            return cityList
        }
    }

    suspend fun getIdProofList():List<IDProofData>{
        var idList:List<IDProofData> = listOf()
        try{
            val apiResponse = appService.getIdProofList().await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                 idList = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<IDProofData>::class.java
                ).toList()

                Log.d(TAG, "api countryList ${idList.size}")
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }finally {
            return idList
        }
    }

    suspend fun registerVisitor(
        firstName: String,
        lastName: String,
        birthYear: String,
        gender: String,
        contactNumber: String,
        address: String,
        pincode: String,
        countryId: String,
        stateId: String,
        districtId: String,
        cityId: String,
        email: String,
        identityId: String,
        idNo: String,
        userId:Int
    ): Resource<Int> {
        try{
            val requestArray =JSONArray()
            val requestJSON = JSONObject()
            requestJSON.put("VISITOR_ID",0)
            requestJSON.put("FIRST_NAME",firstName)
            requestJSON.put("LAST_NAME",lastName)
            requestJSON.put("DOB_YEAR",birthYear)
            requestJSON.put("GENDER",gender)
            requestJSON.put("CONTACT_NO",contactNumber)
            requestJSON.put("ADDRESS",address)
            requestJSON.put("PIN_CODE",pincode)
            requestJSON.put("STATE_ID",stateId)
            requestJSON.put("DISTRICT_ID",districtId)
            requestJSON.put("CITY_ID",cityId)
            requestJSON.put("EMAIL_ID",email)
            requestJSON.put("IDENTITY_ID",identityId)
            requestJSON.put("IDENTITY_NO",idNo)
            requestJSON.put("LOGIN_USER_ID",userId)
            requestArray.put(requestJSON)


            val apiResponse = appService.registerVisitor(
               requestParam = requestArray.toString()
            ).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("OUTPUT") == 1) {
              return  Resource(status = Status.SUCCESS,data = responseObj.getInt("USER_ID"),message = null)
            }else{
                return  Resource(status = Status.ERROR,data = null,message = responseObj.getString("MESSAGE"))
            }
        }catch (ex: NetworkErrorException) {
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

    private  fun postError(message: String): Resource<Int> {
        return Resource(status = Status.ERROR, data = null, message = message)
    }
}