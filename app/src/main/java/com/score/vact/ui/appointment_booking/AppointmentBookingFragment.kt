package com.score.vact.ui.appointment_booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.score.vact.AppExecutors
import com.score.vact.R
import com.score.vact.adapter.appointment_booking.AccompaniedPersonListAdapter
import com.score.vact.adapter.appointment_booking.BelongingsAdapter
import com.score.vact.databinding.AppointmentBookingFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.model.appointment_booking.AccompaniedPersonData
import com.score.vact.model.appointment_booking.CompanyData
import com.score.vact.model.appointment_booking.DepartmentData
import com.score.vact.model.appointment_booking.EmployeeData
import com.score.vact.ui.afterTextChanged
import com.score.vact.util.autoCleared
import kotlinx.android.synthetic.main.appointment_booking_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AppointmentBookingFragment : Fragment(), CoroutineScope, Injectable, View.OnClickListener {
    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AppointmentBookingViewModel
    private lateinit var job: Job
    private var binding by autoCleared<AppointmentBookingFragmentBinding>()
    private var mAccompaniedAdapter by autoCleared<AccompaniedPersonListAdapter>()
    private var mBelongingsAdapter by autoCleared<BelongingsAdapter>()
    private lateinit var mCurrentCalendar: Calendar
    private var mCompanies: List<CompanyData> = listOf()
    private var mDepartments: List<DepartmentData> = listOf()
    private var mEmployees: List<EmployeeData> = listOf()
    private val args by navArgs<AppointmentBookingFragmentArgs>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCurrentCalendar = Calendar.getInstance()
        job = Job()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeCompaniesData()
        observeDepartment()
        observeEmployees()
        observeAccompaniedPerson()

        //This observation is create to automatically check availability
        //for the appointment after selecting tim and validating condition
        observeTimeSelection()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<AppointmentBookingFragmentBinding>(
            inflater,
            R.layout.appointment_booking_fragment,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AppointmentBookingViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.etDate.setOnClickListener(this)
        binding.etTime.setOnClickListener(this)
        // viewModel.selectedTransportationId.postValue(binding.rdPersonal.id)
        viewModel.selectedVehicleTypeId.postValue(binding.rdTwoWhealer.id)
        binding.spnCompany.setOnClickListener(this)
        binding.spnDepartment.setOnClickListener(this)
        binding.spnPerson.setOnClickListener(this)

        args.accompaniedPerson?.let {
            addAccompaniedPerson(args.accompaniedPerson!!)
        }

        getCompanies()
        // getBelongings()
        // observeBelongingsData()


        binding.spnCompany.afterTextChanged {
            updateSelection(it, binding.spnCompany.id)
        }
        binding.spnDepartment.afterTextChanged {
            updateSelection(it, binding.spnDepartment.id)
        }
        binding.spnPerson.afterTextChanged {
            updateSelection(it, spnPerson.id)
        }
        val adapter =
            AccompaniedPersonListAdapter(
                appExecutors = appExecutors
            ) { accompaniedPersonData ->
                viewModel.remove(accompaniedPersonData)
            }
        binding.recyclerView.adapter = adapter
        this.mAccompaniedAdapter = adapter


        //Belongings selection is not required. it shows belongings checkbox list in grid view
        /* val belongingAdapter = BelongingsAdapter(appExecutors = appExecutors) {
             viewModel.updateBelongingsItem(it)
         }
         val layoutManager = FlexboxLayoutManager(this.requireContext())
         layoutManager.flexWrap = FlexWrap.WRAP
         layoutManager.justifyContent = JustifyContent.FLEX_START
         binding.belongingsGrid.layoutManager = layoutManager
         binding.belongingsGrid.adapter = belongingAdapter
         this.mBelongingsAdapter = belongingAdapter
 */

        viewModel.validate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                return@Observer
            } else {
                Log.d(TAG, "isFormValid $it")
            }
        })
        binding.btnAdd?.setOnClickListener(this)

        setFragmentResultListener(AccompaniedPersonFragment.REQUEST_KEY) { key, bundle ->
            // read from the bundle
            addAccompaniedPerson(bundle.getSerializable("person") as AccompaniedPersonData)
        }
    }



    private fun getCompanies() = launch {
        viewModel.getCompanies()
    }

    /*private fun getBelongings() = launch {
        viewModel.getBelongings()
    }*/

    private fun observeCompaniesData() {
        viewModel.companies.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                return@Observer
            }
            mCompanies = it
            setSpinner(it, binding.spnCompany)
        })
    }

   /* private fun observeBelongingsData() {
        viewModel.belongigns.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                return@Observer
            }
            mBelongingsAdapter.submitList(it)
        })
    }*/

    private fun observeDepartment() {
        viewModel.departments.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                return@Observer
            }
            mDepartments = it
            setSpinner(it, binding.spnDepartment)
        })
    }

    private fun observeEmployees() {
        viewModel.employess.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) {
                return@Observer
            }
            mEmployees = it
            setSpinner(it, binding.spnPerson)
        })
    }

    private fun observeAccompaniedPerson() {
        viewModel.accompaniedPersons.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null)
                return@Observer
            mAccompaniedAdapter.submitList(it)
        })
    }

    private fun setSpinner(it: List<Any>, view: AutoCompleteTextView) {
        if (it.isEmpty()) {
            view.setText("")
        }
        val adapter: ArrayAdapter<Any> = ArrayAdapter<Any>(
            this.requireContext(),
            android.R.layout.select_dialog_item,
            it
        )
        view.threshold = 0
        view.setAdapter(adapter)
    }

    private fun updateSelection(text: String, viewId: Int) = launch(Dispatchers.Default) {
        when (viewId) {
            binding.spnCompany.id -> {
                for (i in mCompanies.indices) {
                    if (mCompanies[i].toString().equals(text, true)) {
                        viewModel.setCompany(mCompanies[i])
                        break
                    } else {
                        viewModel.setCompany(null)
                    }
                }
            }
            binding.spnDepartment.id -> {
                for (i in mDepartments.indices) {
                    if (mDepartments[i].toString().equals(text, true)) {
                        viewModel.setDepartment(mDepartments[i])
                        break
                    } else {
                        viewModel.setDepartment(null)
                    }
                }
            }
            binding.spnPerson.id -> {
                for (i in mEmployees.indices) {
                    if (mEmployees[i].toString().equals(text, true)) {
                        viewModel.setEmployee(mEmployees[i])
                        break
                    } else {
                        viewModel.setEmployee(null)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.etDate.id -> selectDate()
            binding.etTime.id -> selectTime()
            binding.spnCompany.id -> spnCompany.showDropDown()
            binding.spnDepartment.id -> spnDepartment.showDropDown()
            binding.spnPerson.id -> spnPerson.showDropDown()
            binding.btnAdd?.id -> {
                this.findNavController().navigate(R.id.action_appointmentBookingFragment_to_accompaniedPersonFragment)
            }
        }
    }

    private fun selectDate() {
        val datePicker = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(p1, p2, p3)
                viewModel.date.postValue(formatter.format(selectedCalendar.timeInMillis))
            },
            mCurrentCalendar.get(Calendar.YEAR),
            mCurrentCalendar.get(Calendar.MONTH),
            mCurrentCalendar.get(Calendar.DAY_OF_YEAR)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun selectTime() {
        val timePicker = TimePickerDialog(
            this.requireContext(),
            TimePickerDialog.OnTimeSetListener { p0, p1, p2 ->
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, p1)
                selectedCalendar.set(Calendar.MINUTE, p2)
                if (selectedCalendar.timeInMillis < System.currentTimeMillis()) {
                    return@OnTimeSetListener
                }
                viewModel.time.postValue(formatter.format(selectedCalendar.timeInMillis))
            },
            mCurrentCalendar.get(Calendar.HOUR_OF_DAY),
            mCurrentCalendar.get(Calendar.MINUTE), true
        )
        //datePicker.time.minDate = mCurrentCalendar.timeInMillis
        timePicker.show()
    }

    private fun observeTimeSelection()  = launch{
        viewModel.canCheckAvailability.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it==null)
                return@Observer

             if(it){
                checkForAvailability()
            }
        })
    }

    private fun checkForAvailability() = launch{
        viewModel.checkAvailability()
    }

    private fun addAccompaniedPerson(accompaniedPerson: AccompaniedPersonData) {
        Handler().postDelayed({
            viewModel.onAccompaniedAdd(accompaniedPerson)
        },200)
    }


}

