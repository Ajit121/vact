package com.score.vact.ui.appointment_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.repository.AppointmentRepo
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import com.score.vact.vo.lazyDeferred
import java.io.IOException
import javax.inject.Inject

class AppointmentsViewModel @Inject constructor(private val mAppointmentRepo: AppointmentRepo) :
    ViewModel() {

    private val _appointments = MutableLiveData<Resource<List<AppointmentData>>>()

    val appointments: LiveData<Resource<List<AppointmentData>>>
        get() = _appointments

    suspend fun getAppointments(date: String) {
        _appointments.postValue(Resource(status = Status.LOADING, data = null, message = null))
        try {
            val appointments = mAppointmentRepo.getAppointments(date)
            appointments.observeForever {
                if (it == null) {
                    return@observeForever
                }
                _appointments.postValue(
                    Resource(
                        status = Status.SUCCESS,
                        data = it,
                        message = null
                    )
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            _appointments.postValue(
                Resource(
                    status = Status.ERROR,
                    data = null,
                    message = ex.message
                )
            )
        }

    }
}
