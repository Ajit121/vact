package com.score.vact.ui.appointment_details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.score.vact.AppExecutors

import com.score.vact.R
import com.score.vact.adapter.appointment_booking.AccompaniedDetailsListAdapter
import com.score.vact.databinding.AppointmentDetailsFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.vo.Status
import kotlinx.android.synthetic.main.appointment_details_fragment.*
import kotlinx.android.synthetic.main.otp_layout.*
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AppointmentDetailsFragment : Fragment(), Injectable, CoroutineScope, View.OnClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AppointmentDetailsViewModel
    private val TAG = javaClass.simpleName
    private val args: AppointmentDetailsFragmentArgs by navArgs()
    private var appointmentId = 0
    private lateinit var job: Job
    private lateinit var binding:AppointmentDetailsFragmentBinding
    private lateinit var mAdapter:AccompaniedDetailsListAdapter
    @Inject
    lateinit var appExecutors:AppExecutors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appointmentId = args.id
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AppointmentDetailsFragmentBinding>(inflater,
        R.layout.appointment_details_fragment,
        container,
        false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AppointmentDetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = AccompaniedDetailsListAdapter(appExecutors = appExecutors)
        mAdapter = adapter
        binding.recyclerView.adapter = mAdapter
        getAppointments()
        observeAppointmentDetails()
    }

    private fun observeAppointmentDetails() {
        viewModel.appointmentDetails.observe(viewLifecycleOwner, Observer {
            if(it==null){
                return@Observer
            }
            if (it.status == Status.ERROR) {
                Snackbar.make(
                    coordinateLayout,
                    it.message ?: "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                mAdapter.submitList(it.data?.detailsData?.accompaniedBy)
            }
        })
    }

    private fun getAppointments() = launch {
        viewModel.getAppointmentDetails(appointmentId)
    }

    override fun onClick(view: View) {


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}
