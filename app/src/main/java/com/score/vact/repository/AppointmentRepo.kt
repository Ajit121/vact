package com.score.vact.repository

import androidx.lifecycle.LiveData
import com.score.vact.datasource.AppointmentDataSource
import com.score.vact.db.AppointmentDao
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.model.appointment.AppointmentDetailsData
import com.score.vact.model.appointment_booking.BelongingsData
import com.score.vact.model.appointment_booking.CompanyData
import com.score.vact.model.appointment_booking.DepartmentData
import com.score.vact.model.appointment_booking.EmployeeData
import com.score.vact.vo.Resource
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class AppointmentRepo @Inject constructor(
    private val mAppointmentDataSource: AppointmentDataSource,
    private val appointmentDao: AppointmentDao
) {
    //Appointment booking fragment
    suspend fun getCompanies(): List<CompanyData> = mAppointmentDataSource.getCompanies()
    suspend fun getDepartment(companyId: String): List<DepartmentData> =
        mAppointmentDataSource.getDepartments(companyId = companyId)

    suspend fun getEmployees(companyId: String, departmentId: String): List<EmployeeData> =
        mAppointmentDataSource.getEmployess(companyId, departmentId)

    suspend fun getBelongings(): LiveData<List<BelongingsData>> {
        val belongings = mAppointmentDataSource.getBelongings()
        appointmentDao.insertBelongings(belongings)
        return appointmentDao.getBelongings()
    }

    //AppointmentFragment
    suspend fun getAppointments(date:String): LiveData<List<AppointmentData>> {
        val appointments = mAppointmentDataSource.getAppointments(date)
        appointmentDao.insertAppointments(appointments)
        return appointmentDao.getAppointments(date)
    }

    suspend fun getAppointmentDetails(appointmentId: Int): LiveData<AppointmentDetailsData> {
        try {
            val details = mAppointmentDataSource.getAppointmentDetails(appointmentId)
            appointmentDao.insertDetails(details)
            return appointmentDao.getAppointmentDetails(appointmentId)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw IOException(ex.message)
        }
    }

    suspend fun checkAppointmentAvailability(
        companyId: String,
        departmentId: String,
        employeeId: String,
        date: String,
        time: String
    ): Resource<Boolean> = mAppointmentDataSource.checkAppointmentAvailability(
        companyId = companyId, departmentId = departmentId, employeeId = employeeId,
        date = date, time = time
    )
}