package com.score.vact.ui.appointment_modify

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.score.vact.R
import com.score.vact.databinding.AppointmentModifyFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.util.autoCleared
import javax.inject.Inject

class AppointmentModifyFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AppointmentModifyViewModel
    private var binding by autoCleared<AppointmentModifyFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AppointmentModifyFragmentBinding>(
            inflater,
            R.layout.appointment_modify_fragment,
            container,
            false
        )
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewmodelFactory).get(AppointmentModifyViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
