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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentUploadPostBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UploadPostFragment : Fragment() {

    private var _binding: FragmentUploadPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UploadPostViewModel by viewModels()
    private var post: Post? = null
    private val args by navArgs<UploadPostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadPostBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        post = Post().apply {
                            userProfileImage = it.result?.userProfileImage
                        }
                    }
                    is Resource.Failure -> {
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.postButtonClickEvent.collect {
                post = setPost()
                post?.let {
                    viewModel.addPostToFirestore(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.addPostFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Post upload!", Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigate(R.id.action_uploadPostFragment_to_feedFragment)
                    }
                    is Resource.Loading -> { binding.progressBarLoading.visibility = View.VISIBLE }
                    else -> {}
                }
            }
        }
    }

    private fun initListeners() {

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

    private fun setPost(): Post? {
        post?.apply {
            title = binding.titleEditText.text.toString()
            content = binding.contentEditText.text.toString()
        }
        return post
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}