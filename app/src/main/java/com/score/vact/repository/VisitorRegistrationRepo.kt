package com.score.vact.repository

import com.score.vact.datasource.VisitorRegistrationDataSource
import com.score.vact.db.RegistrationFormDao
import com.score.vact.model.*
import com.score.vact.vo.Resource
import javax.inject.Inject

class VisitorRegistrationRepo @Inject constructor(
    private val viewRegistrationDataSource: VisitorRegistrationDataSource,
    private val registrationFormDao: RegistrationFormDao,
    private val sharedPrefs: SharedPrefs
) {
    private val TAG = javaClass.simpleName

    suspend fun getCountries(): List<CountryData> =viewRegistrationDataSource.getCountries()

    suspend fun getStates(countryId: String):List<StateData> = viewRegistrationDataSource.getStates(countryId = countryId)

    suspend fun getDistricts(stateId: String):List<DistrictData> = viewRegistrationDataSource.getDistricts(stateId = stateId)

    suspend fun getCities(districtId: String):List<CityData> = viewRegistrationDataSource.getCities(districtId = districtId)

    suspend fun getIdProofList():List<IDProofData> = viewRegistrationDataSource.getIdProofList()

    suspend fun registerVisitor(firstName: String, lastName: String, birthYear: String, gender: String,
        contactNumber: String, address: String, pincode: String, countryId: String, stateId: String,
        districtId: String, cityId: String, email: String, identityId: String, idNo: String
    ): Resource<Int>{

        return viewRegistrationDataSource.registerVisitor(  firstName = firstName, lastName = lastName,
            birthYear = birthYear, gender = gender, contactNumber = contactNumber, address = address, pincode = pincode,
            countryId = countryId, stateId = stateId, districtId = districtId, cityId = cityId,
            email = email, identityId = identityId, idNo = idNo,userId = sharedPrefs.userId)
    }


}