package com.score.vact.model.appointment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "appointment_details")
data class DetailsData(
    @PrimaryKey
    val id: Int,
    @SerializedName("accompanied_by")
    val accompaniedBy: List<AccompaniedBy>,
    val belongings: List<String>,
    val department: String,
    val designation: String,
    val purpose: String,
    @SerializedName("report_to")
    val reportTo: String
)