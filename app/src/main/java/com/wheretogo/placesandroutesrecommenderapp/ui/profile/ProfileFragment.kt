package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentProfilePageBinding
import com.wheretogo.placesandroutesrecommenderapp.ui.auth.SharedAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val sharedAuthViewModel: SharedAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()
        initListeners()
    }

    private fun initCollectors() {

    }

    private fun initListeners() {
        binding.logOutTextView.setOnClickListener {
            sharedAuthViewModel.logout()
        }
    }
}