package com.wheretogo.placesandroutesrecommenderapp.ui.uploadpost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentUploadPostBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPostFragment : Fragment() {

    private var _binding: FragmentUploadPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UploadPostViewModel by viewModels()
    private val args by navArgs<UploadPostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadPostBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setListeners()

        // println(args.email)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.postButtonClickEvent.collect {
                val post = setPost()
                viewModel.addPostToFirestore(args.email, post)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addPostFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {

                    }
                    is Resource.Loading -> {

                    }
                    else -> {}
                }
            }
        }
    }

    private fun setListeners() {

        binding.contentEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }
        binding.titleEditText.doOnTextChanged { _, _, _, _ ->
            setButtonEnable()
        }

    }

    private fun setButtonEnable() {
        binding.uploadButton.isEnabled = (binding.titleEditText.text.isNullOrEmpty().not() &&
                binding.contentEditText.text.isNullOrEmpty().not())
    }

    private fun setPost(): Post {
        return Post(
            title = binding.titleEditText.text.toString(),
            content = binding.contentEditText.text.toString()
        )
    }
}