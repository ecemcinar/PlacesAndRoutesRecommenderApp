package com.wheretogo.placesandroutesrecommenderapp.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentFeedBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Post

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() =_binding!!
    private val adapter: FeedAdapter by lazy { FeedAdapter() }
    private val args by navArgs<FeedFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        setUpRecyclerView()

        println(args.email)

        adapter.setFeedData(
            listOf(
                Post(
                    username = "ecemcinar",
                    title = "PARIS",
                    content = getString(R.string.feed_article_content_sample_text),
                ),
                Post(
                    username = "dogaerdemir",
                    title = "PARIS 1",
                    content = getString(R.string.feed_article_content_sample_text)
                ),
                Post(
                    username = "armancelik",
                    title = "PARIS 2",
                    content = getString(R.string.feed_article_content_sample_text)
                )
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // disables the back arrow
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }
}