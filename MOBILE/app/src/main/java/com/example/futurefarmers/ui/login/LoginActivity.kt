package com.example.futurefarmers.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.futurefarmers.R
import com.example.futurefarmers.databinding.ActivityLoginBinding
import com.example.futurefarmers.ui.AuthModelFactory
import com.example.futurefarmers.ui.main.MainActivity
import com.google.gson.JsonObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        AuthModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnLogin.setOnClickListener{
            var username = binding.etUsername.text.toString()
            var password = binding.etPassword.text.toString()
            val param = JsonObject().apply {
                addProperty("username", username)
                addProperty("password", password)
            }
            loginViewModel.login(param)
            loginViewModel.getLoginResponse().observe(this){
                if (!it.error.toBoolean()){
                    loginViewModel.saveSession(it.token.toString())
                    startActivity(Intent(this,MainActivity::class.java))
                }
                showToast(it.message.toString())
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}