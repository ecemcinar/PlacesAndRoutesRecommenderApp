package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentSetPreferencesBinding

class SetPreferencesFragment : Fragment(), PreferenceButtonClick {

    private var _binding: FragmentSetPreferencesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetPreferencesViewModel by viewModels()
    private lateinit var adapter: SetPreferencesRecyclerAdapter

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

        return binding.root
    }

    private fun setUpRecyclerView() {
        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        binding.setPrefRecyclerView.layoutManager = layoutManager
        binding.setPrefRecyclerView.adapter = adapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPreferenceButtonClick(item: SetPreferencesModel) {
        TODO("Not yet implemented")
    }
}