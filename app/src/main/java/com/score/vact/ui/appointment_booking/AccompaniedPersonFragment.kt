package com.score.vact.ui.appointment_booking

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.score.vact.R
import com.score.vact.databinding.FragmentAccompaniedFormBinding
import com.score.vact.di.Injectable
import com.score.vact.di.ViewModelFactoryModule
import com.score.vact.model.IDProofData
import com.score.vact.ui.afterTextChanged
import kotlinx.android.synthetic.main.visitor_registration_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class AccompaniedPersonFragment : Fragment(), Injectable, CoroutineScope, View.OnClickListener {

    companion object {
        val REQUEST_KEY = "REQUEST_KEY"
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AccompaniedPersonViewModel
    private lateinit var binding: FragmentAccompaniedFormBinding
    private lateinit var job: Job
    private var mIdProofList: List<IDProofData> = listOf()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeIdProofList()
        observeGenderSelection()
        observePersonAdd()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccompaniedFormBinding>(
            inflater, R.layout.fragment_accompanied_form,
            container, false
        )
        this.binding = binding
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AccompaniedPersonViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.spinnerIdProof.afterTextChanged { text ->
            updateSelectedIdProof(text.trim())
        }
        binding.spinnerIdProof.setOnClickListener(this)
    }

    private fun observeIdProofList() = launch {
        viewModel.documentList.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            mIdProofList = it
            setSpinner(it, spinnerIdProof)
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

    private fun updateSelectedIdProof(text: String) = launch(Dispatchers.Default) {
        for (i in mIdProofList.indices) {
            if (mIdProofList[i].toString().equals(text, true)) {
                viewModel.setIdProof(mIdProofList[i])
                break
            } else {
                viewModel.setIdProof(null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id) {
                binding.spinnerIdProof.id -> binding.spinnerIdProof.showDropDown()
            }
        }
    }

    private fun observeGenderSelection() = launch {
        viewModel.selectedGenderId.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                binding.rbMale.id -> viewModel.selectedGenderText.postValue("Male")
                binding.rbFemale.id -> viewModel.selectedGenderText.postValue("Female")
                binding.rbTrans.id -> viewModel.selectedGenderText.postValue("Transgender")
            }
        })
    }

    private fun observePersonAdd() = launch {
        viewModel.addedPerson.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            Snackbar.make(coordinateLayout, "Person added", Snackbar.LENGTH_LONG).show()

            Handler().postDelayed({
                val action =
                    AccompaniedPersonFragmentDirections.actionAccompaniedPersonFragmentToAppointmentBookingFragment(
                        -1,//dummy value because is required param avoided this by checking BookingFragment
                        accompaniedPerson = it
                    )
               // this@AccompaniedPersonFragment.findNavController().navigate(action)
                val bundle = Bundle()
                bundle.putSerializable("person",it)

                setFragmentResult(REQUEST_KEY, bundle)

                // Step 4. Go back to Fragment A
                findNavController().navigateUp()

            }, 500)
        })
    }

}
