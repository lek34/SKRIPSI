package com.example.futurefarmers.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.futurefarmers.ui.control.ConfigFragment
import com.example.futurefarmers.R
import com.example.futurefarmers.ui.setting.SettingFragment
import com.example.futurefarmers.databinding.ActivityMainBinding
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token: String? = null
    lateinit var homeFragment: HomeFragment
    lateinit var configFragment: ConfigFragment
    lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        homeFragment = HomeFragment()
        configFragment = ConfigFragment()
        settingFragment = SettingFragment()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadFragment(HomeFragment())
        mainViewModel.getSession().observe(this){
            token = "Bearer $it"
            if(it == "") {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.bottomNavigationView.setOnItemSelectedListener  {
            try {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        loadFragment(homeFragment)
                        true
                    }
                    R.id.navigation_control -> {
                        loadFragment(configFragment)
                        true
                    } else -> {
                    R.id.navigation_setting
                    loadFragment(settingFragment)
                    true
                }
                }
            } catch (e : Exception) {
                throw e
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(com.google.android.material.R.id.container, fragment)
            transaction.commit()
        }
    }

}