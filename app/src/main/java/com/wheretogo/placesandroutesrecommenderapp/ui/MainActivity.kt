package com.wheretogo.placesandroutesrecommenderapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.ui.auth.SharedAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val sharedViewModel: SharedAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController

       findViewById<BottomNavigationView>(R.id.bottomNavigationView)
           .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, dest, args ->
            when (dest.id) {
                R.id.loginFragment, R.id.signUpFragment, R.id.customizeProfileFragment -> {
                    findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
                    args?.putString("email", sharedViewModel.currentUser?.email.orEmpty())
                }
                else -> {
                    findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                        View.VISIBLE
                    args?.putString("email", sharedViewModel.currentUser?.email.orEmpty())
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.getOnBackPressedDispatcher().onBackPressed()
    }
}