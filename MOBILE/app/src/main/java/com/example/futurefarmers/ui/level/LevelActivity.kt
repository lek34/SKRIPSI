package com.example.futurefarmers.ui.level

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.futurefarmers.R
import com.example.futurefarmers.databinding.ActivityLevelBinding
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.login.LoginActivity
import com.google.gson.JsonObject

class LevelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelBinding
    private var token: String? = null
    private val levelViewModel by viewModels<LevelViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLevelBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        levelViewModel.getSession().observe(this){
            token = "Bearer $it"
            if(it == ""){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                token?.let {
                    levelViewModel.getLevel(it)
                    levelViewModel.getLevelResponse().observe(this){
                        binding.etPhLow.text = it.phLow.toString().toEditable()
                        binding.etPhHigh.text = it.phHigh.toString().toEditable()
                        binding.etTempLow.text = it.temperatureLow.toString().toEditable()
                        binding.etTempHigh.text = it.temperatureHigh.toString().toEditable()
                        binding.etNutrisi.text = it.tds.toString().toEditable()
                    }
                }
            }
        }
        binding.closeButton.setOnClickListener{
           finish()
        }
        binding.btnUpdateLevel.setOnClickListener {
            val phLow = binding.etPhLow.text.toString().toFloat()
            val phHigh= binding.etPhHigh.text.toString().toFloat()
            val tempLow = binding.etTempLow.text.toString().toFloat()
            val tempHigh = binding.etTempHigh.text.toString().toFloat()
            val tds = binding.etNutrisi.text.toString().toFloat()

            val param = JsonObject().apply {
                addProperty("ph_high", phHigh)
                addProperty("ph_low", phLow)
                addProperty("tds", tds)
                addProperty("temp_high", tempHigh)
                addProperty("temp_low", tempLow)
            }
            token?.let { it1 -> levelViewModel.updateLevel(it1,param) }
            levelViewModel.getUpdateLevelResponse().observe(this){
                if (!it.error.toBoolean()){
                    showToast("Level Configuration Berhasil Disimpan")
                }else{
                    showToast("Level Configuration Gagal Disimpan")
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}