package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentSetPreferencesBinding
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetPreferencesFragment : Fragment() {

    private var _binding: FragmentSetPreferencesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetPreferencesViewModel by viewModels()
    private lateinit var adapter: SetPreferencesRecyclerAdapter
    private val args by navArgs<SetPreferencesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSetPreferencesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = SetPreferencesRecyclerAdapter(mutableListOf(), ::onPreferenceButtonClick)
        viewModel.createPrefList()
        lifecycleScope.launch {
            viewModel.prefList.collect {
                it?.let { mutableList ->
                    adapter.setData(mutableList.toList())
                    adapter.notifyDataSetChanged()
                }
            }
        }

        setUpRecyclerView()
        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.setPrefListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), "User preferences are set.", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.feedFragment)
                    }
                    is Resource.Loading -> { binding.progressBarLoading.visibility = View.VISIBLE }
                    else -> {}
                }
            }
        }
    }

    private fun onPreferenceButtonClick(item: SetPreferencesModel) {
        viewModel.setPrefList(item)
        binding.executePendingBindings()
    }

    private fun setListeners() {
        binding.continueButton.setOnClickListener {
            args.userId?.let {
                viewModel.setPrefListToUser(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        binding.setPrefRecyclerView.layoutManager = layoutManager
        binding.setPrefRecyclerView.adapter = adapter
    }
}