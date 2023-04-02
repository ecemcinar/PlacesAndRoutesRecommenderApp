package com.wheretogo.placesandroutesrecommenderapp.ui.customizeprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCustomizeProfileBinding

class CustomizeProfileFragment: Fragment() {

    private var _binding: FragmentCustomizeProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CustomizeProfileViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var selectedImage: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCustomizeProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.uploadClickEvent.collect {
                // findNavController().navigate(R.id.action_customizeProfileFragment_to_setPreferencesFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.imageClickEvent.collect {
                whenImageClicked()
            }
        }
    }

    private fun whenImageClicked() {
        // callback
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if(result.resultCode == AppCompatActivity.RESULT_OK){
                    val intentFromResult = result.data // bize nullable intent donecek
                    if(intentFromResult !=null){
                        selectedImage = intentFromResult.data
                        selectedImage?.let {
                            binding.imageView.setImageURI(it) //bitmape cevirmeden bu sekilde yapilabilir
                            //cunku direkt firebase bunu uri olarak alip upload edebiliyor
                        }
                    }
                }
            })

        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)
    }
}