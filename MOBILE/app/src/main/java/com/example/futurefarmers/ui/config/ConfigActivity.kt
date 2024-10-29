package com.example.futurefarmers.ui.config

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
import com.example.futurefarmers.databinding.ActivityConfigBinding
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.login.LoginActivity
import com.google.gson.JsonObject

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    private var token: String? = null
    private val configViewModel by viewModels<ConfigViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        configViewModel.getSession().observe(this){
            token = "Bearer $it"
            if(it == ""){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                token?.let {
                    configViewModel.getConfig(it)
                    configViewModel.getConfigResponse().observe(this){
                        binding.etPhDown.text = it.phDown.toString().toEditable()
                        binding.etPhUp.text = it.phUp.toString().toEditable()
                        binding.etNutrisiA.text = it.nutA.toString().toEditable()
                        binding.etNutrisiB.text = it.nutB.toString().toEditable()
                        binding.etFan.text = it.fan.toString().toEditable()
                        binding.etLight.text = it.light.toString().toEditable()
                    }
                }
            }
        }
        binding.closeButton.setOnClickListener{
           finish()
        }
        binding.btnUpdateConfig.setOnClickListener {
            val phUp = binding.etPhUp.text.toString().toFloat()
            val phDown= binding.etPhDown.text.toString().toFloat()
            val nutA = binding.etNutrisiA.text.toString().toFloat()
            val nutB = binding.etNutrisiB.text.toString().toFloat()
            val fan = binding.etFan.text.toString().toFloat()
            val light = binding.etLight.text.toString().toFloat()

            val param = JsonObject().apply {
                addProperty("ph_up", phUp)
                addProperty("ph_down", phDown)
                addProperty("nut_a", nutA)
                addProperty("nut_b", nutB)
                addProperty("fan", fan)
                addProperty("light", light)
            }
            token?.let { it1 -> configViewModel.updateConfig(it1,param) }
            configViewModel.getUpdateConfigResponse().observe(this){
                if (!it.error.toBoolean()){
                    showToast("Timeout Configuration Berhasil Disimpan")
                }else{
                    showToast("Timeout Configuration Gagal Disimpan")
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}