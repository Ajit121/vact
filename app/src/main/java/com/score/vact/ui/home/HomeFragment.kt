package com.score.vact.ui.home

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.score.vact.R
import com.score.vact.databinding.HomeFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.ui.MainActivity
import com.score.vact.ui.afterTextChanged
import com.score.vact.vo.Status
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.number_layout.*
import kotlinx.android.synthetic.main.otp_layout.*
import kotlinx.android.synthetic.main.otp_layout.otpLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), Injectable, CoroutineScope, View.OnClickListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeViewModel
    private val TAG = javaClass.simpleName
    private val keyValues = SparseArray<String>()
    private var otpInputConnection: InputConnection? = null
    private var mobileInputConnection: InputConnection? = null
    private lateinit var otpBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var numberBottomSheetBehavior: BottomSheetBehavior<*>
    private val CAMERA_REQUEST_CODE = 1024
    private lateinit var binding: HomeFragmentBinding
    private lateinit var job: Job

    private var verifiedNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )
        this.binding = binding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG,"onActivityCreated called")
        observePhoneVerification()
        observerOtpVerification()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        otpBottomSheetBehavior = BottomSheetBehavior.from(otpLayout)
        numberBottomSheetBehavior = BottomSheetBehavior.from(mobileLayout)

        otpBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        numberBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        otpInputConnection = etPinView.onCreateInputConnection(EditorInfo())
        mobileInputConnection = etNumber.onCreateInputConnection(EditorInfo())
        assignClickEvent()
        assignKeyValues()

        btnAppointment.setOnClickListener {
        //    numberBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            val action = HomeFragmentDirections.actionHomeFragmentToAppointmentBookingFragment(12,accompaniedPerson = null)
            this.findNavController().navigate(action)
        }

        etNumber.afterTextChanged {
            if (it.length == 10) {
                val act = activity as MainActivity
                act.showAlert("An OTP will be sent to ${etNumber.text.toString().trim()}",
                    DialogInterface.OnClickListener { p0, p1 ->
                       viewModel.verifyPhoneNumber(it)
                    })
            }
        }

        etPinView.afterTextChanged {
            if (it.length == 4) {
                viewModel.verifyOtp(verifiedNumber,it)
            }
        }

        scanView.setOnClickListener(this)
        numberBottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if(p1 == BottomSheetBehavior.STATE_HIDDEN){
                    etNumber.setText("")
                }
            }

        })
        otpBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
                 if(p1 == BottomSheetBehavior.STATE_HIDDEN){
                    etPinView.setText("")
                }
            }
        })


    }

    private fun observePhoneVerification() {
        viewModel.phoneNumberVerificationResponse.observe(viewLifecycleOwner, Observer {
            if(it==null){
                return@Observer
            }
            Log.d(TAG,"observePhoneVerification called with value ${it.status}")

            if(it.status==Status.LOADING){
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }
            if(it.status==Status.SUCCESS){
                verifiedNumber = etNumber.text.toString()
                numberBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Handler().postDelayed({
                    otpBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                },500)
            }else if(it.status == Status.ERROR){
                Toast.makeText(this.requireContext(),it.message!!,Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun observerOtpVerification() {

        viewModel.otpVerificationResponse.observe(viewLifecycleOwner, Observer {
            if(it==null){
                return@Observer
            }
            Log.d(TAG,"observerOtpVerification called with value ${it.status}")

            if(it.status==Status.LOADING){
                otpProgressBar.visibility = View.VISIBLE
            }else{
                otpProgressBar.visibility = View.GONE
            }
            if(it.status==Status.SUCCESS){
                otpBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Handler().postDelayed({
                    when (it.data) {
                        0 -> {
                            val action = HomeFragmentDirections.actionHomeFragmentToRegistrationFragment(verifiedNumber)
                            this.findNavController().navigate(action)
                        }
                        1 -> {
                            val action = HomeFragmentDirections.actionHomeFragmentToSurveyFragment(it.data)
                            this.findNavController().navigate(action)
                        }
                        else -> {
                            Toast.makeText(this.requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                        }
                    }
                },500)
            }else if(it.status == Status.ERROR){
                Toast.makeText(this.requireContext(),it.message!!,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun assignClickEvent() {
        btn1!!.setOnClickListener(this)
        btn2!!.setOnClickListener(this)
        btn3!!.setOnClickListener(this)
        btn4!!.setOnClickListener(this)
        btn5!!.setOnClickListener(this)
        btn6!!.setOnClickListener(this)
        btn7!!.setOnClickListener(this)
        btn8!!.setOnClickListener(this)
        btn9!!.setOnClickListener(this)
        btn0!!.setOnClickListener(this)

        btnDelete!!.setOnClickListener(this)

        btn1a!!.setOnClickListener(this)
        btn2a!!.setOnClickListener(this)
        btn3a!!.setOnClickListener(this)
        btn4a!!.setOnClickListener(this)
        btn5a!!.setOnClickListener(this)
        btn6a!!.setOnClickListener(this)
        btn7a!!.setOnClickListener(this)
        btn8a!!.setOnClickListener(this)
        btn9a!!.setOnClickListener(this)
        btn0a!!.setOnClickListener(this)

        btnDeletea!!.setOnClickListener(this)
    }

    private fun assignKeyValues() {

        keyValues.put(R.id.btn1, "1")
        keyValues.put(R.id.btn2, "2")
        keyValues.put(R.id.btn3, "3")
        keyValues.put(R.id.btn4, "4")
        keyValues.put(R.id.btn5, "5")
        keyValues.put(R.id.btn6, "6")
        keyValues.put(R.id.btn7, "7")
        keyValues.put(R.id.btn8, "8")
        keyValues.put(R.id.btn9, "9")
        keyValues.put(R.id.btn0, "0")

        keyValues.put(R.id.btn1a, "1")
        keyValues.put(R.id.btn2a, "2")
        keyValues.put(R.id.btn3a, "3")
        keyValues.put(R.id.btn4a, "4")
        keyValues.put(R.id.btn5a, "5")
        keyValues.put(R.id.btn6a, "6")
        keyValues.put(R.id.btn7a, "7")
        keyValues.put(R.id.btn8a, "8")
        keyValues.put(R.id.btn9a, "9")
        keyValues.put(R.id.btn0a, "0")

    }

    private fun navigateToSurveyPage() {
        val navController = this.findNavController()
        etPinView.setText("")
        navController.navigate(R.id.action_homeFragment_to_surveyFragment)
    }

    private fun navigateToRegistrationPage() {
        etPinView.setText("")
        this.findNavController().navigate(R.id.action_homeFragment_to_registrationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.clear()
    }

    private fun scanBarCode() {


        //TODO navigate for scanner fragment in production
        //   val navController = this.findNavController()
        // navController.navigate(R.id.action_homeFragment_to_scannerFragment)


        //For navigating to surveyFragment for development purpose
        val action = HomeFragmentDirections.actionHomeFragmentToSurveyFragment(12)
        this.findNavController().navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "permission result")
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBarCode()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onClick(view: View) {
        if(view.id == binding.scanView.id){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                    return
                }
            }
            scanBarCode()
        }
        if (view.id == R.id.btnDelete) {
            val selectedText = otpInputConnection!!.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                otpInputConnection!!.deleteSurroundingText(1, 0)
            } else {
                otpInputConnection!!.commitText("", 1)
            }

        } else if (view.id == R.id.btnDeletea) {
            val selectedText = mobileInputConnection!!.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                mobileInputConnection!!.deleteSurroundingText(1, 0)
            } else {
                mobileInputConnection!!.commitText("", 1)
            }

        } else {
            try {
                val value = keyValues[view.id]
                Log.d(TAG, "value is $value")
                if (otpBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    otpInputConnection!!.commitText(value, 1)
                } else if (numberBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    mobileInputConnection!!.commitText(value, 1)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }


    private fun hideOtpSheet() {
        if (otpBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            otpBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun hideMobileSheet() {
        if (numberBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            numberBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}
