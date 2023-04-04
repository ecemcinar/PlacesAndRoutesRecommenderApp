package com.wheretogo.placesandroutesrecommenderapp.ui.uploadpost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentUploadPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPostFragment : Fragment() {

    private var _binding: FragmentUploadPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UploadPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadPostBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        
        binding.contentEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.titleEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setButtonEnable() {
        binding.uploadButton.isEnabled = (binding.titleEditText.text.isNullOrEmpty().not() &&
                binding.contentEditText.text.isNullOrEmpty().not())
    }
}