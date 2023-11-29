package com.score.vact.model.appointment_booking

import java.io.Serializable


data class AccompaniedPersonData(
    var name: String, var doy: String, var gender: String, var number: String,
    var email: String, var address: String, var docTypeId: String, var docId: String
) : Serializable