package com.wheretogo.placesandroutesrecommenderapp.ui.customizeprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCustomizeProfileBinding

class CustomizeProfileFragment: Fragment() {

    private var _binding: FragmentCustomizeProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CustomizeProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCustomizeProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.uploadClickEvent.collect {
                findNavController().navigate(R.id.action_customizeProfileFragment_to_setPreferencesFragment)
            }
        }
    }
}