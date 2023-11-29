package com.score.vact.ui.appointment_booking

import android.util.Log
import androidx.lifecycle.*
import com.score.vact.model.appointment_booking.*
import com.score.vact.repository.AppointmentRepo
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AppointmentBookingViewModel @Inject constructor(
    private val mAppointmentRepo: AppointmentRepo
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private val mBelongingList = mutableListOf<String>()
    val date = MutableLiveData<String>("")
    val time = MutableLiveData<String>("")
    val purpose = MutableLiveData<String>("")
    val selectedTransportationId = MutableLiveData<Int>()
    val selectedVehicleTypeId = MutableLiveData<Int>()
    val vehicleNumber = MutableLiveData("")
    val accompaniedByName = MutableLiveData("")
    val accompaniedByNumber = MutableLiveData("")
    val errorMessage = MutableLiveData<String>("")

    private val _belongings = MutableLiveData<List<BelongingsData>>()
    val belongigns: LiveData<List<BelongingsData>>
        get() = _belongings

    private var _companies = MutableLiveData<List<CompanyData>>()
    val companies: LiveData<List<CompanyData>>
        get() = _companies
    private val selectedCompany = MutableLiveData<CompanyData>(null)

    private var _departments = MutableLiveData<List<DepartmentData>>()
    val departments: LiveData<List<DepartmentData>>
        get() = _departments
    private val selectedDepartment = MutableLiveData<DepartmentData>(null)

    private var _emplyoees = MutableLiveData<List<EmployeeData>>()
    val employess: LiveData<List<EmployeeData>>
        get() = _emplyoees

    private val selectedEmployee = MutableLiveData<EmployeeData>(null)

    private val _appointmentAvailabilityResponse = MutableLiveData<Resource<Boolean>>()
    val appointmentAvailabilityResponse: LiveData<Resource<Boolean>>
        get() = _appointmentAvailabilityResponse


    val canCheckAvailability = MediatorLiveData<Boolean>().apply {
        addSource(selectedCompany){
            value = performAvailabilityCheck()
        }
        addSource(selectedDepartment){
            value = performAvailabilityCheck()
        }
        addSource(selectedEmployee){
            value = performAvailabilityCheck()
        }
        addSource(date){
            value = performAvailabilityCheck()
        }
        addSource(time) {
            value = performAvailabilityCheck()
        }
    }


    private val _accompaniedPersons = MutableLiveData<List<AccompaniedPersonData>>()
    val accompaniedPersons: LiveData<List<AccompaniedPersonData>>
        get() = _accompaniedPersons

    private fun updateSelectedList(it: Boolean, date: String): List<String> {
        if (it) {
            mBelongingList.add(date)
        } else {
            mBelongingList.remove(date)
        }
        return mBelongingList
    }

    fun onAccompaniedAdd(accompaniedPerson: AccompaniedPersonData) {
        val existingList = mutableListOf<AccompaniedPersonData>()
        existingList.addAll(if (_accompaniedPersons.value == null) emptyList() else _accompaniedPersons.value!!)
       existingList.add(accompaniedPerson)
        _accompaniedPersons.postValue(existingList)
    }

    suspend fun getCompanies() {
        val companies = mAppointmentRepo.getCompanies()
        _companies.postValue(companies)
    }

    suspend fun getBelongings() {
        val belongings = mAppointmentRepo.getBelongings()
        belongings.observeForever {
            if (it == null) {
                return@observeForever
            }
            _belongings.postValue(it)
        }

    }

    suspend fun setCompany(companyData: CompanyData?) {
        selectedDepartment.postValue(null)
        selectedEmployee.postValue(null)
        selectedCompany.postValue(companyData)
        if (companyData != null) {
            getDepartments(companyData)
        } else {
            _departments.postValue(emptyList())
            _emplyoees.postValue(emptyList())
        }
    }

    private suspend fun getDepartments(companyData: CompanyData) {
        val departments = mAppointmentRepo.getDepartment(companyId = companyData.id)
        _departments.postValue(departments)
    }

    suspend fun setDepartment(departmentData: DepartmentData?) {
        selectedEmployee.postValue(null)
        selectedDepartment.postValue(departmentData)
        if (departmentData != null) {
            getEmployees(departmentData.id)
        } else {
            _emplyoees.postValue(emptyList())
        }
    }

    private suspend fun getEmployees(departmentId: String) {
        val employees = mAppointmentRepo.getEmployees(selectedCompany.value!!.id, departmentId)
        _emplyoees.postValue(employees)
    }

    fun setEmployee(employeeData: EmployeeData?) {
        selectedEmployee.postValue(employeeData)
    }

    fun remove(accompaniedPersonData: AccompaniedPersonData) {
        val existingList = mutableListOf<AccompaniedPersonData>()
        existingList.addAll(_accompaniedPersons.value!!)
        existingList.remove(accompaniedPersonData)
        _accompaniedPersons.postValue(existingList)
    }


    val validate = MediatorLiveData<Boolean>().apply {
        /* addSource(selectedTransportationId) {
             value = isValid()
         }
         addSource(vehicleNumber) {
             value = isValid()
         }*/
        addSource(selectedEmployee) {
            value = isValid()
        }
        addSource(selectedDepartment) {
            value = isValid()
        }

        addSource(selectedCompany) {
            value = isValid()
        }
        addSource(date) {
            value = isValid()
        }
        addSource(time) {
            value = isValid()
        }
        addSource(appointmentAvailabilityResponse){
            value = isValid()
        }
        addSource(purpose) {
            value = isValid()
        }
    }

    private fun isValid(): Boolean {
        errorMessage.postValue("")
        if (selectedCompany.value == null) {
            errorMessage.postValue("Please select company")
            return false
        }
        if (selectedDepartment.value == null) {
            errorMessage.postValue("Please select department")
            return false
        }
        if (selectedEmployee.value == null) {
            errorMessage.postValue("Please select person to meet")
        }
        if (date.value.isNullOrEmpty()) {
            return false
        }
        if (time.value.isNullOrEmpty()) {
            return false
        }
        if (appointmentAvailabilityResponse.value == null || appointmentAvailabilityResponse.value!!.status == Status.ERROR) {
            errorMessage.postValue("Appointment not available at selected date and time")
            return false
        }
        if (purpose.value.isNullOrEmpty()) {
            return false
        }

        /* if (selectedTransportationId.value == R.id.rdPersonal) {
             if (vehicleNumber.value.isNullOrEmpty()) {
                 return false
             }
         }*/


        return true
    }

    fun updateBelongingsItem(updatedBelonging: BelongingsData) {
        viewModelScope.launch(Dispatchers.Default) {
            _belongings.value?.let {
                it.forEach { data ->
                    if (updatedBelonging.id == data.id) {
                        val updatedData = data.copy(isSelected = updatedBelonging.isSelected)
                        it as MutableList
                        it[it.indexOf(data)] = updatedData
                        _belongings.postValue(it)
                    }
                }
            }
        }
    }

    private fun performAvailabilityCheck(): Boolean {
        return (selectedCompany.value != null && selectedDepartment.value != null &&
                selectedEmployee.value != null && !date.value.isNullOrEmpty() &&
                !time.value.isNullOrEmpty())
    }

    suspend fun checkAvailability() {
        _appointmentAvailabilityResponse.postValue(
            Resource(
                status = Status.LOADING,
                data = null,
                message = null
            )
        )
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date.value!!)!!
        val formattedData = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(date.time)

         val time = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time.value!!)!!
        val formattedTime = SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(time)


        Log.d(TAG,"formatted date and time $formattedData and $formattedTime")
        val isAvailable = mAppointmentRepo.checkAppointmentAvailability(
            companyId = selectedCompany.value!!.id,
            departmentId = selectedDepartment.value!!.id, employeeId = selectedEmployee.value!!.id,
            date = formattedData, time = formattedTime!!
        )
        _appointmentAvailabilityResponse.postValue(isAvailable)
    }

}
