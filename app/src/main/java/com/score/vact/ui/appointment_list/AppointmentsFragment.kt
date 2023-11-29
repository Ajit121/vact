package com.score.vact.ui.appointment_list

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.score.vact.AppExecutors

import com.score.vact.R
import com.score.vact.adapter.AppointmentListAdapter
import com.score.vact.databinding.AppointmentBookingFragmentBinding
import com.score.vact.databinding.AppointmentsFragmentBinding
import com.score.vact.databinding.VisitorRegistrationFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.util.autoCleared
import com.score.vact.vo.Status
import kotlinx.android.synthetic.main.appointments_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AppointmentsFragment : Fragment(), Injectable, CoroutineScope {
    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var job: Job
    private lateinit var viewModel: AppointmentsViewModel
    private var binding by autoCleared<AppointmentsFragmentBinding>()
    private var mAppointmentAdapter by autoCleared<AppointmentListAdapter>()
    private val dateFormatter = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

    @Inject
    lateinit var appExecutors: AppExecutors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AppointmentsFragmentBinding>(
            inflater,
            R.layout.appointments_fragment,
            container,
            false
        )
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AppointmentsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = AppointmentListAdapter(appExecutors = appExecutors,
            onClick = {
                val action =
                    AppointmentsFragmentDirections.actionAppointmentsFragment2ToAppointmentDetailsFragment2(
                        it.id
                    )
                this.findNavController().navigate(action)
            },
            onActionClick = { id, action ->
                when(action){
                    Action.MODIFY->{
                        this.findNavController().navigate(R.id.action_appointmentsFragment2_to_appointmentModifyFragment)
                    }
                }
            })
        binding.recyclerView.adapter = adapter
        this.mAppointmentAdapter = adapter
        getAppointments(dateFormatter.format(Calendar.getInstance().timeInMillis))
        observerAppointments()
    }


    private fun getAppointments(date: String) = launch {
        viewModel.getAppointments(date)
    }

    private fun observerAppointments() {
        viewModel.appointments.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            if (it.status == Status.SUCCESS) {
                mAppointmentAdapter.submitList(it.data)
            } else if (it.status == Status.ERROR) {
                Snackbar.make(binding.coordinateLayout, it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.appointments_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_date -> {
                showCalendar()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCalendar() {
        val calender = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { _, p1, p2, p3 ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(p1, p2, p3)
                val date = dateFormatter.format(selectedCalendar.timeInMillis).toString()
                getAppointments(date)
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_YEAR)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }
}
