package com.example.futurefarmers.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.futurefarmers.R
import com.example.futurefarmers.databinding.FragmentSettingBinding
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.config.ConfigActivity
import com.example.futurefarmers.ui.level.LevelActivity
import com.example.futurefarmers.ui.login.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentSettingBinding
    private var token: String? = null
    private val settingViewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        settingViewModel.getSession().observe(viewLifecycleOwner) {
            token = "Bearer $it"
            if (it == "") {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }

        binding.imgLogout.setOnClickListener {
            settingViewModel.logout()
            showToast("Logout Berhasil")
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.tvLogout.setOnClickListener {
            settingViewModel.logout()
            showToast("Logout Berhasil")
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        binding.imageView3.setOnClickListener {
            navToConfigTimeout()
        }

        binding.tvRelayConfig.setOnClickListener {
            navToConfigTimeout()
        }

        binding.deskripsiRelayConfig.setOnClickListener {
            navToConfigTimeout()
        }

        binding.imageView4.setOnClickListener {
            navToLevelConfig()
        }

        binding.deskripsiLevelConfig.setOnClickListener {
            navToLevelConfig()
        }

        binding.tvLevelConfig.setOnClickListener {
            navToLevelConfig()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navToConfigTimeout() {
        startActivity(Intent(requireContext(), ConfigActivity::class.java))
    }

    private fun navToLevelConfig() {
        startActivity(Intent(requireContext(), LevelActivity::class.java))
    }
}