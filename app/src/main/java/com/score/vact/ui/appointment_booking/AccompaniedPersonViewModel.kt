package com.score.vact.ui.appointment_booking

import androidx.lifecycle.*
import com.score.vact.R
import com.score.vact.model.IDProofData
import com.score.vact.model.appointment_booking.AccompaniedPersonData
import com.score.vact.repository.AppointmentRepo
import com.score.vact.repository.VisitorRegistrationRepo
import com.score.vact.vo.lazyDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class AccompaniedPersonViewModel @Inject constructor(val userRegistrationRepo: VisitorRegistrationRepo) :
    ViewModel() {

    val fullname = MutableLiveData<String>("")
    val year = MutableLiveData<String>("")
    val selectedGenderId = MutableLiveData<Int>(R.id.rbMale)
    val contactNumber = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val address = MutableLiveData<String>("")
    val idNo = MutableLiveData<String>("")
    val selectedGenderText = MutableLiveData<String>()

    val isValidForm = MediatorLiveData<Boolean>().apply {
        addSource(fullname) {
            value = isValid()
        }
        addSource(year) {
            value = isValid()
        }
        addSource(selectedGenderId) {
            value = isValid()
        }
        addSource(contactNumber) {
            value = isValid()
        }
        addSource(email) {
            value = isValid()
        }
        addSource(address) {
            value = isValid()
        }
        /*addSource(selectedIdProof){
            value = isValid()
        }*/
        addSource(idNo) {
            value = isValid()
        }
    }
    val errorMessage = MutableLiveData<String>()
    val documentList = MutableLiveData<List<IDProofData>>()

    private var _selectedIdProof = MutableLiveData<IDProofData>(null)
    val selectedIdProof: LiveData<IDProofData>
        get() = _selectedIdProof

    suspend fun setIdProof(idProofData: IDProofData?) {
        _selectedIdProof.postValue(idProofData)
    }

    private val _addedPerson = MutableLiveData<AccompaniedPersonData>()
    val addedPerson: LiveData<AccompaniedPersonData>
        get() = _addedPerson

    init {
        getDocumentList()
    }

    private fun getDocumentList() = viewModelScope.launch {
        val docList = userRegistrationRepo.getIdProofList()
        documentList.postValue(docList)
    }

    override fun onCleared() {
        super.onCleared()
        try {
            viewModelScope.cancel()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun isValid(): Boolean {
        errorMessage.postValue("")
        if (selectedIdProof.value == null) {
            errorMessage.postValue("Please select ID proof")
            return false
        }

        if (fullname.value.isNullOrEmpty()) {
            return false
        }
        if (year.value.isNullOrEmpty()) {
            return false
        }
        if (contactNumber.value.isNullOrEmpty()) {
            return false
        }
        if (email.value.isNullOrEmpty()) {
            return false
        }
        if (address.value.isNullOrEmpty()) {
            return false
        }
        if (idNo.value.isNullOrEmpty()) {
            return false
        }

        return true
    }

    fun addPerson() {
        val name = fullname.value!!
        val doy = year.value!!
        val gender = selectedGenderText.value!!
        val number = contactNumber.value!!
        val email = email.value!!
        val address = address.value!!
        val docTypeId = selectedIdProof.value!!.id
        val docId = idNo.value!!
        val accompaniedPersonData = AccompaniedPersonData(name, doy, gender, number, email, address, docTypeId, docId)
        _addedPerson.postValue(accompaniedPersonData)
    }
}