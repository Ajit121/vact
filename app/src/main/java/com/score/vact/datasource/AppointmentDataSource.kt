package com.score.vact.datasource

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import com.score.vact.api.AppService
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.model.appointment.DetailsData
import com.score.vact.model.appointment_booking.BelongingsData
import com.score.vact.model.appointment_booking.CompanyData
import com.score.vact.model.appointment_booking.DepartmentData
import com.score.vact.model.appointment_booking.EmployeeData
import com.score.vact.vo.Resource
import com.score.vact.vo.ResponseException
import com.score.vact.vo.Status
import org.json.JSONObject
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class AppointmentDataSource @Inject constructor(private val appService: AppService) {
    suspend fun getCompanies(): List<CompanyData> {
        var companies = listOf<CompanyData>()
        try {
            val apiResponse = appService.getCompanies().await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                companies = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<CompanyData>::class.java
                ).toList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            return companies
        }
    }

    suspend fun getDepartments(companyId: String): List<DepartmentData> {
        var departments = listOf<DepartmentData>()
        try {
            val apiResponse = appService.getDepartments(companyId).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                departments = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<DepartmentData>::class.java
                ).toList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            return departments
        }
    }

    suspend fun getEmployess(companyId: String, departmentId: String): List<EmployeeData> {
        var employees = listOf<EmployeeData>()
        try {
            val apiResponse = appService.getEmployees(companyId, departmentId).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                employees = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<EmployeeData>::class.java
                ).toList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            return employees
        }
    }

    suspend fun getBelongings(): List<BelongingsData> {
        var belongings = listOf<BelongingsData>()
        try {
            val apiResponse = appService.getBelongings().await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("Output") == 1) {
                belongings = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<BelongingsData>::class.java
                ).toList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            return belongings
        }
    }

    suspend fun getAppointments(date: String): List<AppointmentData> {
        var appointments = listOf<AppointmentData>()
        try {
            //TODO get appointments from API
            //   val apiResponse = appService.getAppointments().await()

            val responseObj = JSONObject(getDummyAppointments())
            if (responseObj.getInt("Output") == 1) {
                appointments = Gson().fromJson(
                    responseObj.getJSONArray("Data").toString(),
                    Array<AppointmentData>::class.java
                ).toList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            return appointments
        }

    }

    private fun getDummyAppointments(): String {
        /*
        {
  "Output": 1,
  "Data": [
    {
      "id": 1,
      "image_url": "",
      "name": "Ajit Jaiswal",
      "number": "9876543210",
      "email": "ajit.jaiswal@score.co.in",
      "address": "Kolkata",
      "date": "May 25 2020",
      "time": "04:30 PM",
      "status": 1
    },
    {
      "id": 2,
      "image_url": "",
      "name": "Debjit maity",
      "number": "9876543210",
      "email": "debjit.mainti@score.co.in",
      "address": "Kolkata",
      "date": "May 25 2020",
      "time": "05:30 PM",
      "status": 2
    }
  ]
}
         */



        return "{\n" +
                "  \"Output\": 1,\n" +
                "  \"Data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"image_url\": \"\",\n" +
                "      \"name\": \"Ajit Jaiswal\",\n" +
                "      \"number\": \"9876543210\",\n" +
                "      \"email\": \"ajit.jaiswal@score.co.in\",\n" +
                "      \"address\": \"Kolkata\",\n" +
                "      \"date\": \"May 25 2020\",\n" +
                "      \"time\": \"04:30 PM\",\n" +
                "      \"status\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"image_url\": \"\",\n" +
                "      \"name\": \"Debjit maity\",\n" +
                "      \"number\": \"9876543210\",\n" +
                "      \"email\": \"debjit.mainti@score.co.in\",\n" +
                "      \"address\": \"Kolkata\",\n" +
                "      \"date\": \"May 25 2020\",\n" +
                "      \"time\": \"05:30 PM\",\n" +
                "      \"status\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    }


    fun getAppointmentDetails(appointmentId: Int): DetailsData {
        try {
            //TODO get appointments from API
            //   val apiResponse = appService.getAppointments().await()

            val responseObj = JSONObject(getDummyAppointmentDetails())
            if (responseObj.getInt("Output") == 1) {
                val details = Gson().fromJson(
                    responseObj.getJSONObject("Data").toString(),
                    DetailsData::class.java
                )
                return details
            } else {
                throw IOException("Something went wrong")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw IOException("Something went wrong")
        }
    }


    private fun getDummyAppointmentDetails(): String {
        return "{\n" +
                "  \"Output\": 1,\n" +
                "  \"Data\": {\n" +
                "    \"id\":1,\n" +
                "    \"accompanied_by\": [\n" +
                "      {\n" +
                "        \"name\": \"Ajit Jaiswal\",\n" +
                "        \"number\": \"9876543210\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Debjit Maity\",\n" +
                "        \"number\": \"9876543210\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"report_to\": \"Subhajit Sarkar\",\n" +
                "    \"designation\": \"HOD\",\n" +
                "    \"department\": \"IT Department\",\n" +
                "    \"purpose\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\n" +
                "    \"belongings\": [\n" +
                "      \"Mobile\",\n" +
                "      \"Bag\",\n" +
                "      \"Laptop\"\n" +
                "    ]\n" +
                "  }\n" +
                "}"
        /*
        {
      "Output": 1,
      "Data": {
        "id":1,
        "accompanied_by": [
          {
            "name": "Ajit Jaiswal",
            "number": "9876543210"
          },
          {
            "name": "Debjit Maity",
            "number": "9876543210"
          }
        ],
        "report_to": "Subhajit Sarkar",
        "designation": "HOD",
        "department": "IT Department",
        "purpose": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        "belongings": [
          "Mobile",
          "Bag",
          "Laptop"
        ]
      }
    }
         */
    }

    suspend fun checkAppointmentAvailability(
        companyId: String,
        departmentId: String,
        employeeId: String,
        date: String,
        time: String
    ): Resource<Boolean> {
        try {
            val apiResponse = appService.checkAppointmentAvailability(
                companyId, employeeId, date, time
            ).await()
            val responseObj = JSONObject(apiResponse)
            if (responseObj.getInt("OUTPUT") == 1) {
                return Resource(
                    status = Status.SUCCESS,
                    data = true,
                    message = null
                )
            } else {
                return Resource(
                    status = Status.ERROR,
                    data = false,
                    message = responseObj.getString("MESSAGE")
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
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            return postError(message = "Something went wrong")
        }
    }

    private fun postError(message: String): Resource<Boolean> {
        return Resource(status = Status.ERROR, data = null, message = message)
    }
}