package com.score.vact.ui.visitor_registration

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.score.vact.R
import com.score.vact.databinding.VisitorRegistrationFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.model.*
import com.score.vact.ui.afterTextChanged
import com.score.vact.util.autoCleared
import com.score.vact.vo.Status
import kotlinx.android.synthetic.main.visitor_registration_fragment.*
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VisitorRegistrationFragment : Fragment(), Injectable, CoroutineScope, View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val TAG = javaClass.simpleName
    private lateinit var viewModel: VisitorRegistrationViewModel
    private lateinit var job: Job
    private var binding by autoCleared<VisitorRegistrationFragmentBinding>()
    lateinit var countryAdapter: ArrayAdapter<String>
    private var mCountryList: List<CountryData> = listOf()
    private var mStateList: List<StateData> = listOf()
    private var mDistrictList: List<DistrictData> = listOf()
    private var mCityList: List<CityData> = listOf()
    private var mIdProofList: List<IDProofData> = listOf()
    private val ADD_PROFILE_IMAGE_REQUEST = 1231
    private val ADD_DOCUMENT_IMAGE_REQUEST = 1232
    private var profileImageFilePath: String = ""
    private var docImageFilePath: String = ""
    private val args by navArgs<VisitorRegistrationFragmentArgs>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<VisitorRegistrationFragmentBinding>(
            inflater,
            R.layout.visitor_registration_fragment,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeCountryList()
        observeIdProofList()
        observeStateList()
        observeDistrictList()
        observeCityList()
        observeGenderSelection()
        observeRegistrationResponse()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(VisitorRegistrationViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.checkedGender.postValue(R.id.rbMale)
        getCountries()
        getIdProofList()
        viewModel.primaryNumber.postValue(args.phoneNumber)
        binding.btnRegister.setOnClickListener(this)
        binding.spinnerCountry.setOnClickListener(this)
        binding.spinnerState.setOnClickListener(this)
        binding.spinnerDistrict.setOnClickListener(this)
        binding.spinnerCity.setOnClickListener(this)
        binding.spinnerIdProof.setOnClickListener(this)
        binding.spinnerCountry.afterTextChanged { text ->
            updateSelectedCountry(text.trim())
        }
        binding.spinnerState.afterTextChanged {
            updateSelectedState(it.trim())
        }
        binding.spinnerDistrict.afterTextChanged {
            updateSelectedDistrict(it.trim())
        }
        binding.spinnerCity.afterTextChanged {
            updateSelectedCity(it.trim())
        }
        binding.spinnerIdProof.afterTextChanged {
            updateSelectedIdProof(it.trim())
        }


        binding.imgProfile.setOnClickListener(this)
        binding.imgDoc.setOnClickListener(this)
    }

    private fun getCountries() = launch {
        viewModel.getCountries()
    }


    private fun getIdProofList() = launch {
        viewModel.getIdProofList()
    }


    private fun observeCountryList() = launch {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            mCountryList = it
            setSpinner(it, spinnerCountry)
        })
    }

    private fun observeIdProofList() = launch {
        viewModel.idProofDocuments.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            mIdProofList = it
            setSpinner(it, spinnerIdProof)
        })
    }

    private fun observeStateList() = launch {
        viewModel.states.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            Log.d(TAG, "state list found ${it.size}")
            mStateList = it

            setSpinner(it, spinnerState)
        })

    }

    private fun observeDistrictList() = launch {
        viewModel.district.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            Log.d(TAG, "districtList list found ${it.size}")
            mDistrictList = it

            setSpinner(it, spinnerDistrict)
        })

    }

    private fun observeCityList() = launch {
        viewModel.cities.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            Log.d(TAG, "city list found ${it.size}")
            mCityList = it

            setSpinner(it, spinnerCity)
        })

    }

    private fun observeGenderSelection() = launch {
        viewModel.checkedGender.observe(viewLifecycleOwner, Observer {
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

    private fun setSpinner(it: List<Any>, view: AutoCompleteTextView) {
        if (it.isEmpty()) {
            view.setText("")
        }
        val adapter: ArrayAdapter<Any> = ArrayAdapter<Any>(
            this@VisitorRegistrationFragment.requireContext(),
            android.R.layout.select_dialog_item,
            it
        )

        view.threshold = 0
        view.setAdapter(adapter)
    }

    private fun updateSelectedCountry(text: String) = launch(Dispatchers.Default) {
        for (i in mCountryList.indices) {
            if (mCountryList[i].toString().equals(text, true)) {
                viewModel.setCountry(mCountryList[i])
                break
            } else {
                viewModel.setCountry(null)
            }
        }
    }

    private fun updateSelectedState(text: String) = launch(Dispatchers.Default) {
        for (i in mStateList.indices) {
            if (mStateList[i].toString().equals(text, true)) {
                viewModel.setState(mStateList[i])
                break
            } else {
                viewModel.setState(null)
            }
        }
    }

    private fun updateSelectedDistrict(text: String) = launch(Dispatchers.Default) {
        for (i in mDistrictList.indices) {
            if (mDistrictList[i].toString().equals(text, true)) {
                viewModel.setDistrict(mDistrictList[i])
                break
            } else {
                viewModel.setDistrict(null)
            }
        }
    }

    private fun updateSelectedCity(text: String) = launch(Dispatchers.Default) {
        for (i in mCityList.indices) {
            if (mCityList[i].toString().equals(text, true)) {
                viewModel.setCity(mCityList[i])
                break
            } else {
                viewModel.setCity(null)
            }
        }
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


    override fun onClick(v: View?) {
        if (v is AutoCompleteTextView) {
            v.showDropDown()
        }
        when (v!!.id) {
            binding.btnRegister.id -> {
                viewModel.registerVisitor()
            }

            binding.imgProfile.id -> selectImage(ADD_PROFILE_IMAGE_REQUEST)
            binding.imgDoc.id -> selectImage(ADD_DOCUMENT_IMAGE_REQUEST)
        }
    }

    private fun selectImage(selectionMode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionGranted()) {

                addImage(selectionMode)
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), selectionMode
                )
            }
            return
        }
        addImage(selectionMode)
    }

    private fun permissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED))
    }


    private fun addImage(selectionMode: Int) = launch {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = async { createImageFile(selectionMode) }.await()

                    photoFile?.let {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this@VisitorRegistrationFragment.requireContext(),
                            "com.score.vact.fileprovider",
                            it
                        )

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            intent.clipData = ClipData.newRawUri("", photoURI);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        this@VisitorRegistrationFragment.startActivityForResult(
                            intent,
                            selectionMode
                        )
                    }

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private suspend fun createImageFile(selectionMode: Int): File? {
        val timeStamp: String =
            SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date());
        var imageFileName: String =
            if (selectionMode == ADD_PROFILE_IMAGE_REQUEST) "IMG_" else "DOC_" + timeStamp + "_";
        var storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        var image: File = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        if (selectionMode == ADD_PROFILE_IMAGE_REQUEST) {
            profileImageFilePath = image.absolutePath
        } else {
            docImageFilePath = image.absolutePath
        }


        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ADD_PROFILE_IMAGE_REQUEST || requestCode == ADD_DOCUMENT_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                addImage(requestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_PROFILE_IMAGE_REQUEST) {
                if (File(profileImageFilePath).length() >= 0) {
                    if (profileImageFilePath.isNotEmpty() && File(profileImageFilePath).exists()) {
                        viewModel.profileImagePath.postValue(profileImageFilePath)
                    }
                }
            } else if (requestCode == ADD_DOCUMENT_IMAGE_REQUEST) {
                if (File(docImageFilePath).length() >= 0) {
                    if (docImageFilePath.isNotEmpty() && File(docImageFilePath).exists()) {
                        viewModel.documentImagePath.postValue(docImageFilePath)
                    }
                }
            }
        }
    }

    private fun observeRegistrationResponse() = launch {
        viewModel.registrationResponse.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            if (it.status == Status.SUCCESS) {
                val action = VisitorRegistrationFragmentDirections.actionRegistrationFragmentToAppointmentBookingFragment(it.data!!,accompaniedPerson = null)
                this@VisitorRegistrationFragment.findNavController().navigate(action)
            } else if (it.status == Status.ERROR) {
                Snackbar.make(binding.coordinateLayout,it.message!!,Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
