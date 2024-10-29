package com.example.futurefarmers.ui.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.futurefarmers.R
import com.example.futurefarmers.databinding.ActivityAddPlantBinding
import com.example.futurefarmers.ui.AuthModelFactory
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.login.LoginActivity
import com.example.futurefarmers.ui.login.LoginViewModel
import com.example.futurefarmers.ui.main.MainActivity
import com.google.gson.JsonObject

class AddPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantBinding
    private var token: String? = null
    private val plantViewModel by viewModels<PlantViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        plantViewModel.getSession().observe(this){
            token = it
            if(token == ""){
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            token = "Bearer $it"
        }
        binding.buttonAddPlant.setOnClickListener{
            val nama = binding.etTanaman.text.toString()
            val tanggal = binding.etTanggal.text.toString()
            val umur = binding.etWaktuPanen.text.toString()
            val param = JsonObject().apply {
                addProperty("nama", nama)
                addProperty("tanggal", tanggal)
                addProperty("umur", umur.toFloat())
            }
            token?.let { it1 -> plantViewModel.addPlant(it1, param) }
            plantViewModel.getPostPlantResponse().observe(this){
                if (!it.error.toBoolean()){
                    showToast(it.message.toString())
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    showToast(it.message.toString())
                }
            }
        }
        binding.closeButton.setOnClickListener{
            finish()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}