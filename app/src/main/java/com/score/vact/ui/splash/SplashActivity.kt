package com.score.vact.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.score.vact.R
import com.score.vact.ui.MainActivity
import com.score.vact.ui.login.LoginActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        Handler().postDelayed({
            if (viewModel.isLoggedIn) {
                startActivity(Intent(this,MainActivity::class.java))
                this.finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }, 3000)
    }
}
