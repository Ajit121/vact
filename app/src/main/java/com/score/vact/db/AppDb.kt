/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.score.vact.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.model.CountryData
import com.score.vact.model.QuestionData
import com.score.vact.model.appointment.DetailsData
import com.score.vact.model.appointment_booking.BelongingsData

/**
 * Main database description.
 */
@Database(
    entities = [CountryData::class, AppointmentData::class,
        DetailsData::class, BelongingsData::class,QuestionData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class, AccompaniedByDataConverter::class,
    AnswerConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun registrationFormData(): RegistrationFormDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun surveyDao():SurveyDao
}
