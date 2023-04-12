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

@AndroidEntryPoint
class SetPreferencesFragment : Fragment(), PreferenceButtonClick {

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

        viewModel.createPrefList()
        lifecycleScope.launchWhenStarted {
            viewModel.prefList.collect {
                it?.let { mutableList ->
                    adapter.setData(mutableList.toList())
                    adapter.notifyDataSetChanged()
                }
            }
        }
        adapter = SetPreferencesRecyclerAdapter(viewModel.prefList.value, this)
        setUpRecyclerView()
        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.setPrefListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireActivity(), "User preferences are set.", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.feedFragment)
                    }
                    is Resource.Loading -> {

                    }
                    else -> {}
                }
            }
        }
    }

    override fun onPreferenceButtonClick(item: SetPreferencesModel) {
        item.isSelected = (item.isSelected != true)
        viewModel.setPrefList(item)
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