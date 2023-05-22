package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentProfilePageBinding
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.ui.auth.SharedAuthViewModel
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val sharedAuthViewModel: SharedAuthViewModel by viewModels()
    private val args by navArgs<ProfileFragmentArgs>()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.user = user
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.userId?.let { viewModel.getUser(it) }
        initCollectors()
        initListeners()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.getUserFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.user = it.result
                        binding.progressBarLoading.visibility = View.GONE
                        binding.executePendingBindings()
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initListeners() {
        binding.logOutTextView.setOnClickListener {
            sharedAuthViewModel.logout()
        }
    }
}