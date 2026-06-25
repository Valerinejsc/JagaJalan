package com.example.jagajalan.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jagajalan.R
import com.example.jagajalan.databinding.ActivityProfileBinding
import com.example.jagajalan.utils.BottomNavHelper
import com.example.jagajalan.utils.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefsHelper = SharedPreferencesHelper(this)
        val fullName = auth.currentUser?.displayName ?: "Tidak ada nama"
        val email = prefsHelper.getEmail()

        binding.tvFullName.text = fullName
        binding.tvEmail.text = email
        binding.tvAvatar.text = if (fullName.isNotEmpty())
            fullName[0].uppercaseChar().toString() else "U"
        binding.tvDisplayName.text = fullName

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            prefsHelper.clearUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        BottomNavHelper.setup(this, binding.bottomNavigation, R.id.nav_profile)
    }
}