package com.score.vact.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.score.vact.R
import com.score.vact.databinding.ActivityLoginBinding
import com.score.vact.repository.SharedPrefs
import com.score.vact.ui.AppointmentListActivity
import com.score.vact.ui.MainActivity
import com.score.vact.vo.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {
    private val TAG = javaClass.simpleName

    lateinit var job: Job
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private lateinit var loginViewModel: LoginViewModel
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        job = Job()
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        val savedDummy = sharedPrefs.userName

        loginViewModel.userResponse.observe(this, Observer {
            if(it==null){
                return@Observer
            }
            if(it.status==Status.SUCCESS){
                if(it.data!!.isAdmin==1){
                    startActivity(Intent(this,AppointmentListActivity::class.java))
                    this.finish()
                }else {
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }
            }else if(it.status == Status.ERROR){
                Toast.makeText(this@LoginActivity,it.message,Toast.LENGTH_LONG).show()
            }
        })
        binding.viewmodel = loginViewModel
        binding.lifecycleOwner  = this

    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
        }
        return super.dispatchTouchEvent(ev)
    }
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
