package com.wheretogo.placesandroutesrecommenderapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentSignUpBinding
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()
    private val sharedViewModel: SharedAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        setListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // TODO
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        lifecycleScope.launchWhenStarted {
            viewModel.loginClickEvent.collect {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.signUpFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        viewModel.addUserFlow.collect { res ->
                            when (res) {
                                is Resource.Failure -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        res.exception.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                is Resource.Success -> {
                                    Toast.makeText(requireActivity(), "User created and added to Firestore", Toast.LENGTH_SHORT)
                                        .show()
                                    findNavController().popBackStack()
                                    findNavController().navigate(R.id.customizeProfileFragment)
                                }
                                is Resource.Loading -> { }
                                else -> { }
                            }
                        }
                    }
                    is Resource.Loading -> { }
                    else -> { }
                }
            }
        }
    }

    private fun setListeners() {
        binding.emailAddressEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.nameEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.surnameEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.passwordEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.confirmPasswordEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.agreeTermsCheckBox.setOnCheckedChangeListener { _, _ ->
            setButtonEnable()
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailAddressEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val surname = binding.surnameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(requireActivity(), "Passwords must be matched!", Toast.LENGTH_LONG)
                    .show()
            } else {
                sharedViewModel.signup(name = "$name $surname",
                    email = email,
                    password = password
                )
                viewModel.addUserToFirestore(email = email,
                    nameAndSurname = "$name $surname")
            }
        }
    }

    private fun setButtonEnable() {
        binding.signUpButton.isEnabled =
            (binding.passwordEditText.text.isNullOrEmpty().not() &&
                    binding.nameEditText.text.isNullOrEmpty().not() &&
                    binding.surnameEditText.text.isNullOrEmpty().not() &&
                    binding.confirmPasswordEditText.text.isNullOrEmpty().not() &&
                    binding.emailAddressEditText.text.isNullOrEmpty().not() &&
                    binding.agreeTermsCheckBox.isChecked)
    }
}