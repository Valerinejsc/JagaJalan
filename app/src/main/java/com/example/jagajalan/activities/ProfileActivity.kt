package com.example.jagajalan.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jagajalan.databinding.ActivityProfileBinding
import com.example.jagajalan.utils.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.example.jagajalan.utils.BottomNavHelper
import com.example.jagajalan.R

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tambahkan di onCreate() setelah setContentView
        BottomNavHelper.setup(this, binding.bottomNavigation, R.id.nav_profile)

        val prefsHelper = SharedPreferencesHelper(this)
        binding.tvEmail.text = prefsHelper.getEmail()
        binding.tvUserId.text = auth.currentUser?.uid ?: ""

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            prefsHelper.clearUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}