package com.score.vact.model.appointment

import androidx.room.Embedded
import androidx.room.Relation

data class AppointmentDetailsData(
    @Embedded
    val appointmentData: AppointmentData,
    @Relation(parentColumn = "id", entityColumn = "id")
    val detailsData: DetailsData?
)