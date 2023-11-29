package com.score.vact.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.model.appointment.AppointmentDetailsData
import com.score.vact.model.appointment.DetailsData
import com.score.vact.model.appointment_booking.BelongingsData

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentData>)

    @Query("select * from appointments where date =:date")
    fun getAppointments(date: String): LiveData<List<AppointmentData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(detailsData: DetailsData)

    @Query("select * from appointments where id =:appointmentId")
    fun getAppointmentDetails(appointmentId: Int): LiveData<AppointmentDetailsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBelongings(belongings:List<BelongingsData>)

    @Query("select * from belongings_master")
    fun getBelongings():LiveData<List<BelongingsData>>
}