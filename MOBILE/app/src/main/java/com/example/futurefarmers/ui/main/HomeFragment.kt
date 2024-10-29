package com.example.futurefarmers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.futurefarmers.R
import com.example.futurefarmers.data.response.DataResponse
import com.example.futurefarmers.databinding.FragmentHomeBinding
import com.example.futurefarmers.databinding.FragmentSettingBinding
import com.example.futurefarmers.ui.ViewModelFactory
import com.example.futurefarmers.ui.login.LoginActivity
import com.example.futurefarmers.ui.plant.AddPlantActivity
import com.example.futurefarmers.ui.setting.SettingViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeBinding
    private var token: String? = null
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.getSession().observe(viewLifecycleOwner){
            token = "Bearer $it"
            if(it == ""){
                startActivity(Intent(requireContext(),LoginActivity::class.java))
                requireActivity().finish()
        }else{
            token?.let {
                 mainViewModel.tanaman(it)
                    mainViewModel.dashboard(it)
                    mainViewModel.getDataDashboard().observe(this) {
                        updateUI(it)
                    }
                    mainViewModel.getDataPlant().observe(this){
                        binding.tvSayur.text = it.nama.toString()
                        binding.tvPanen.text = it.panen.toString() + " Hari"
                        binding.tvUmur.text = it.umur.toString() + " Hari"
                    }
                }
            }
        }
        binding.cvSedangMenanam.setOnClickListener{
            startActivity(Intent(requireContext(), AddPlantActivity::class.java))
        }
    }
        private fun updateUI(data: DataResponse) {
        // Update UI with the latest data
        binding.tvAngkaSuhu.text = data.suhu.toString()
        binding.tvAngkaKelembapan.text = data.kelembapan.toString()
        binding.tvAngkaTingkatKeasaman.text = data.ph.toString()
        binding.tvAngkaKesehatan.text = data.tds.toString()
    }

}