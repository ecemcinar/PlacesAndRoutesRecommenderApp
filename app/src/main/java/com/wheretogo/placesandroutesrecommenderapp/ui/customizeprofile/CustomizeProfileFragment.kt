package com.wheretogo.placesandroutesrecommenderapp.ui.customizeprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCustomizeProfileBinding

class CustomizeProfileFragment: Fragment() {

    private var _binding: FragmentCustomizeProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CustomizeProfileViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

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
                requestPermission()
            }
        }
    }

    // Kullanıcı galerisine erişim izni verilmediyse, izin isteği gösterilir
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            openGallery()
        }
    }

    // ACTION_GET_CONTENT intent'ini kullanarak galeri uygulamasını açar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    // İzin isteği sonucunu kontrol eder
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireActivity(), "Galeriye erişim izni reddedildi.", Toast.LENGTH_SHORT).show()
            }
        }

    // Seçilen resmi kullanmak için buraya geleceğiz
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data
                binding.imageView.setImageURI(imageUri)
            }
        }
}