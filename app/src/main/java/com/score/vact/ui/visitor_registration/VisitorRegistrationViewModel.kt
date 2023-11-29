package com.score.vact.ui.visitor_registration

import android.util.Log
import androidx.lifecycle.*
import com.score.vact.model.*
import com.score.vact.repository.VisitorRegistrationRepo
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

class VisitorRegistrationViewModel @Inject constructor(private val visitorRegistrationRepo: VisitorRegistrationRepo) :
    ViewModel() {
    private val TAG = javaClass.simpleName

    val checkedGender = MutableLiveData<Int>()

    val selectedGenderText = MutableLiveData<String>()

    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
    val dob = MutableLiveData<String>("")
    val primaryNumber = MutableLiveData<String>("")
    val alternativeNumber = MutableLiveData<String>("")
    val address = MutableLiveData<String>("")
    val pincode = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val alternateEmail = MutableLiveData<String>("")
    val idNo = MutableLiveData<String>("")
    val profileImagePath = MutableLiveData<String>("")
    val documentImagePath = MutableLiveData<String>("")
    val errorMessage = MutableLiveData<String>("")

    private val _countries = MutableLiveData<List<CountryData>>()
    val countries: LiveData<List<CountryData>>
        get() = _countries

    private var _selectedCountryData = MutableLiveData<CountryData>(null)
    val selectedCountryData: LiveData<CountryData>
        get() = _selectedCountryData

    private val _idProofDocuments = MutableLiveData<List<IDProofData>>()
    val idProofDocuments: LiveData<List<IDProofData>>
        get() = _idProofDocuments

    private var _selectedIdProof = MutableLiveData<IDProofData>(null)
    val selectedIdProof: LiveData<IDProofData>
        get() = _selectedIdProof

    private val _states = MutableLiveData<List<StateData>>()
    val states: LiveData<List<StateData>>
        get() = _states

    private var _selectedStateData = MutableLiveData<StateData>(null)
    val selectedStateData: LiveData<StateData>
        get() = _selectedStateData

    private val _district = MutableLiveData<List<DistrictData>>()
    val district: LiveData<List<DistrictData>>
        get() = _district

    private var _selectedDistrictData = MutableLiveData<DistrictData>(null)
    val selectedDistrictData: LiveData<DistrictData>
        get() = _selectedDistrictData

    private val _cities = MutableLiveData<List<CityData>>()
    val cities: LiveData<List<CityData>>
        get() = _cities

    private var _selectedCityData = MutableLiveData<CityData>(null)
    val selectedCityData: LiveData<CityData>
        get() = _selectedCityData


    suspend fun getCountries() {
        val countries = visitorRegistrationRepo.getCountries()
        _countries.postValue(countries)
    }

    suspend fun getIdProofList() {
        val ids = visitorRegistrationRepo.getIdProofList()
        _idProofDocuments.postValue(ids)
    }


    suspend fun setCountry(countryData: CountryData?) {
        _selectedCountryData.postValue(countryData)
        if (countryData != null) {
            val stateList = visitorRegistrationRepo.getStates(countryData.id)
            _states.postValue(stateList)
        } else {
            _states.postValue(emptyList())
            _district.postValue(emptyList())
            _cities.postValue(emptyList())
        }
    }

    suspend fun setState(stateData: StateData?) {
        _selectedStateData.postValue(stateData)
        if (stateData != null) {
            val districtList = visitorRegistrationRepo.getDistricts(stateData.id)
            _district.postValue(districtList)
        } else {
            _district.postValue(emptyList())
            _cities.postValue(emptyList())

        }
    }

    suspend fun setDistrict(districtData: DistrictData?) {
        _selectedDistrictData.postValue(districtData)
        if (districtData != null) {
            val cityList = visitorRegistrationRepo.getCities(districtData.id)
            Log.d(
                TAG,
                "stateListCount for $districtData id is ${districtData.id} is ${cityList.size}"
            )
            _cities.postValue(cityList)
        } else {
            _cities.postValue(emptyList())
        }
    }

    suspend fun setCity(cityData: CityData?) {
        _selectedCityData.postValue(cityData)
    }

    suspend fun setIdProof(idProofData: IDProofData?) {
        _selectedIdProof.postValue(idProofData)
    }

    private val _registrationResponse = MutableLiveData<Resource<Int>>()
    val registrationResponse
        get() = _registrationResponse

    val validate = MediatorLiveData<Boolean>().apply {
        /* addSource(documentImagePath){
             value = isFormValid()
         }*/
        addSource(idNo) {
            value = isFormValid()
        }
        addSource(selectedIdProof) {
            value = isFormValid()
        }
        addSource(email) {
            value = isFormValid()
        }
        addSource(pincode) {
            value = isFormValid()
        }
        addSource(selectedCityData) {
            value = isFormValid()
        }
        addSource(selectedDistrictData) {
            value = isFormValid()
        }
        addSource(selectedStateData) {
            value = isFormValid()
        }
        addSource(selectedCountryData) {
            value = isFormValid()
        }
        addSource(address) {
            value = isFormValid()
        }
        addSource(checkedGender) {
            value = isFormValid()
        }
        addSource(dob) {
            value = isFormValid()
        }
        addSource(lastName) {
            value = isFormValid()
        }
        addSource(firstName) {
            value = isFormValid()
        }
        /* addSource(profileImagePath){
             value = isFormValid()
         }*/


    }

    private fun isFormValid(): Boolean {
        errorMessage.postValue("")
        /* if(profileImagePath.value.isNullOrEmpty()){
             errorMessage.postValue("Please select profile image")
             return false
         }*/

        if (selectedCountryData.value == null) {
            errorMessage.postValue("Please select country")
            return false
        }
        if (selectedStateData.value == null) {
            errorMessage.postValue("Please select state")
            return false
        }
        if (selectedDistrictData.value == null) {
            errorMessage.postValue("Please select district")
            return false
        }
        if (selectedCityData.value == null) {
            errorMessage.postValue("Please select city")
            return false
        }

        if (selectedIdProof.value == null) {
            errorMessage.postValue("Please select ID proof")
            return false
        }

        /* if(documentImagePath.value.isNullOrEmpty()){
             errorMessage.postValue("Please select document image")
             return false
         }*/

        if (firstName.value.isNullOrEmpty()) {
            return false
        }
        if (lastName.value.isNullOrEmpty()) {
            return false
        }
        if (dob.value.isNullOrEmpty()) {
            return false
        }
        if (address.value.isNullOrEmpty()) {
            return false
        }
        if (idNo.value.isNullOrEmpty()) {
            return false
        }
        if (pincode.value.isNullOrEmpty()) {
            return false
        }
        return true
    }

    fun registerVisitor() = viewModelScope.launch {
        _registrationResponse.postValue(
            Resource(
                status = Status.LOADING,
                data = null,
                message = null
            )
        )
        try {
            val visitorRegistrationResponse = visitorRegistrationRepo.registerVisitor(
                firstName = firstName.value?:"",
                lastName = lastName.value?:"",
                birthYear = dob.value?:"",
                gender = selectedGenderText.value?:"",
                contactNumber = primaryNumber.value?:"",
                address = address.value?:"",
                pincode = pincode.value?:"",
                countryId = selectedCountryData.value!!.id,
                stateId = selectedStateData.value!!.id,
                districtId = selectedDistrictData.value!!.id,
                cityId = selectedCityData.value!!.id,
                email = email.value?:"",
                identityId = selectedIdProof.value!!.id,
                idNo = idNo.value?:""
            )
            _registrationResponse.postValue(visitorRegistrationResponse)
        } catch (ex: Exception) {
            _registrationResponse.postValue(
                Resource(
                    status = Status.ERROR,
                    data = null,
                    message = ex.message
                )
            )
        }
    }
}
