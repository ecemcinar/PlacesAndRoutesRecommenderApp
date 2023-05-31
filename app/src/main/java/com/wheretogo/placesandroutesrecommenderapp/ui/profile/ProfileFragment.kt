package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentProfilePageBinding
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.ui.auth.SharedAuthViewModel
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.checkin.CheckInFragment
import com.wheretogo.placesandroutesrecommenderapp.ui.profile.adapter.CheckInAdapter
import com.wheretogo.placesandroutesrecommenderapp.ui.profile.adapter.PostAdapter
import com.wheretogo.placesandroutesrecommenderapp.ui.profile.adapter.PrefAdapter
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val sharedAuthViewModel: SharedAuthViewModel by viewModels()
    private val postAdapter: PostAdapter by lazy { PostAdapter() }
    private val checkInAdapter: CheckInAdapter by lazy { CheckInAdapter() }
    private val prefAdapter: PrefAdapter by lazy { PrefAdapter()}

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
        setUpRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.userId?.let {
            viewModel.getUser(it)
            viewModel.getUserPostList(it)
            viewModel.getUserCheckInList(it)
        }
        initCollectors()
        initListeners()
    }

    private fun initCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.getUserFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.user = it.result
                        it.result?.prefList?.let { userPref -> viewModel.setUserPreferencesList(userPref) }
                        prefAdapter.setPrefList(viewModel.userPreferencesList)
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

        lifecycleScope.launchWhenStarted {
            viewModel.getUserPostListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        it.result?.let { it1 -> postAdapter.setPostList(it1) }
                        binding.executePendingBindings()
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getUserCheckInListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        it.result?.let { it1 -> checkInAdapter.setCheckInList(it1) }
                        binding.executePendingBindings()
                        it.result?.let { categoryList -> getUserCheckInCategories(categoryList) }
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getLocationListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        viewModel.setLocationList(it.result)
                            val location = viewModel.getRandomLocationFromLocationList()
                            location?.let {
                            val fragment = CheckInFragment()
                            val bundle = Bundle()
                            bundle.apply {
                                this.putParcelable("recommendedLocation", location)
                            }
                            fragment.arguments = bundle
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.checkInFragment, bundle)
                        }?: kotlin.run {
                                val builder = AlertDialog.Builder(requireContext())
                                builder
                                    .setTitle("Sorry")
                                    .setMessage("We currently do not have a place to recommend to you.")
                                builder.setPositiveButton(android.R.string.ok) { dialog, which ->

                                }
                                builder.show()
                            }
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
            findNavController().navigate(R.id.loginFragment)
        }

        binding.suggestMe.setOnClickListener {
            viewModel.getLocationList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserCheckInCategories(list: List<CheckIn>) {
        var categoryList = mutableListOf<String>()
        for (i in list) {
            i.category?.let {
                categoryList.add(it)
            }
        }
        viewModel.setUserCheckInCategoryList(categoryList)
    }

    private fun setUpRecyclerView() {
        binding.postRecyclerView.adapter = postAdapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)

        binding.checkInRecyclerView.adapter = checkInAdapter
        binding.checkInRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)

        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        binding.userPrefRecyclerView.layoutManager = layoutManager
        binding.userPrefRecyclerView.adapter = prefAdapter
    }
}