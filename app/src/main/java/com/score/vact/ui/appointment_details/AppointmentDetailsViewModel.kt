package com.score.vact.ui.appointment_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score.vact.model.appointment.AppointmentDetailsData
import com.score.vact.repository.AppointmentRepo
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import com.score.vact.vo.lazyDeferred
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class AppointmentDetailsViewModel @Inject constructor(private val mAppointmentRepo: AppointmentRepo) :
    ViewModel() {

    private val _appointmentDetails = MutableLiveData<Resource<AppointmentDetailsData>>()
    val appointmentDetails: LiveData<Resource<AppointmentDetailsData>>
        get() = _appointmentDetails

    suspend fun getAppointmentDetails(appointmentId: Int) {
        _appointmentDetails.postValue(
            Resource(
                status = Status.LOADING,
                data = null,
                message = null
            )
        )
        try {
            val details = mAppointmentRepo.getAppointmentDetails(appointmentId)
            details.observeForever {
                if(it==null){
                    return@observeForever
                }else if(it.detailsData==null){
                    _appointmentDetails.postValue(
                        Resource(
                            status = Status.ERROR,
                            data = null,
                            message ="No details found"
                        )
                    )
                    return@observeForever
                }
                _appointmentDetails.postValue(
                    Resource(
                        status = Status.SUCCESS,
                        data = it,
                        message = null
                    )
                )
            }
        } catch (ex: IOException) {
            _appointmentDetails.postValue(
                Resource(
                    status = Status.ERROR,
                    data = null,
                    message = ex.message
                )
            )
        }catch (ex: Exception){
            _appointmentDetails.postValue(
                Resource(
                    status = Status.ERROR,
                    data = null,
                    message ="Something went wrong"
                )
            )
        }
    }
}
