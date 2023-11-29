package com.score.vact.api

import androidx.lifecycle.LiveData
import com.score.vact.model.CountryData
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AppService {

    @GET("Appointment/CheckLogin")
    fun loginAsync(
        @Query("UserName") username: String,
        @Query("Password") password: String
    ): Deferred<String>

    @GET("Appointment/GetCountry")
    fun getCountries(): Deferred<String>

    @GET("Appointment/GetState")
    fun getStates(@Query("CountryId") countryId: String): Deferred<String>

    @GET("Appointment/GetDistrict")
    fun getDistricts(@Query("StateId") stateId: String): Deferred<String>

    @GET("Appointment/GetCity")
    fun getCities(@Query("DistrictId") districtId: String): Deferred<String>

    @GET("Appointment/GetIdentityType")
    fun getIdProofList(): Deferred<String>

    @GET("Appointment/GetCompanyMaster")
    fun getCompanies(): Deferred<String>

    @GET("Appointment/GetDepartment")
    fun getDepartments(@Query("CompanyId") companyId: String): Deferred<String>

    @GET("Appointment/GetEmployee")
    fun getEmployees(
        @Query("CompanyId") companyId: String,
        @Query("DepartmentId") departmentId: String
    ): Deferred<String>

    @GET("Appointment/SendOTP")
    fun verifyPhoneNumber(@Query("strMobileno") number: String): Deferred<String>

    @GET("Appointment/CheckOTP")
    fun verifyOTP(
        @Query("MobileNo") number: String,
        @Query("OTP") otp: String
    ): Deferred<String>

    @GET("Appointment/GetBelongingMaster")
    fun getBelongings(): Deferred<String>

    @POST("Appointment/SaveVisitor")
    fun registerVisitor(@Query("VisitorDetails") requestParam: String): Deferred<String>

    @POST()
    fun getSurveyQuestions(): Deferred<String>

    @GET("Appointment/CheckAvailability")
    fun checkAppointmentAvailability(
        @Query("CompanyId") companyId: String,
       // @Query("") departmentId: String,
        @Query("EmployeeId") employeeId: String,
        @Query("AppointmentDate") date: String,
        @Query("AppointmentTime") time: String
    ): Deferred<String>
}