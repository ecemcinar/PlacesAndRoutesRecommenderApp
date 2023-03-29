package com.wheretogo.placesandroutesrecommenderapp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentLoginBinding
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedAuthViewModel by viewModels()
    private var emailInput: String? = null
    private var passwordInput: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = sharedViewModel
        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            sharedViewModel.loginFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Directing...", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.otherOptionButtonClick.collect {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.signInButton.setOnClickListener {
            emailInput = binding.emailAddressEditText.text.toString()
            passwordInput = binding.passwordEditText.text.toString()
            sharedViewModel.login(emailInput!!, passwordInput!!)
        }

        binding.emailAddressEditText.addTextChangedListener(loginTextWatcher)
        binding.passwordEditText.addTextChangedListener(loginTextWatcher)
    }

    private fun setInputs() {
        emailInput = binding.emailAddressEditText.text.toString()
        passwordInput = binding.passwordEditText.text.toString()
    }

    private val loginTextWatcher = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            setInputs()
            binding.signInButton.isEnabled = emailInput.isNullOrEmpty().not() && passwordInput.isNullOrEmpty().not()
        }

        override fun afterTextChanged(p0: Editable?) {
            //
        }
    }
}