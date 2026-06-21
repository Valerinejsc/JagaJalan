package com.example.jagajalan.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jagajalan.MainActivity
import com.example.jagajalan.admin.AdminDashboardActivity
import com.example.jagajalan.databinding.ActivityLoginBinding
import com.example.jagajalan.utils.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Email dan password wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    binding.btnLogin.isEnabled = true

                    if (task.isSuccessful) {

                        val user = auth.currentUser

                        val prefsHelper = SharedPreferencesHelper(this)

                        prefsHelper.saveUser(
                            userId = user?.uid ?: "",
                            userName = user?.displayName ?: email,
                            email = user?.email ?: ""
                        )

                        // DEBUG
                        Toast.makeText(
                            this,
                            "Login sebagai: ${user?.email}",
                            Toast.LENGTH_LONG
                        ).show()

                        // LOGIN ADMIN
                        if (user?.email?.trim()?.lowercase() ==
                            "admin@jagajalan.com"
                        ) {

                            startActivity(
                                Intent(
                                    this,
                                    AdminDashboardActivity::class.java
                                )
                            )

                        } else {

                            startActivity(
                                Intent(
                                    this,
                                    MainActivity::class.java
                                )
                            )

                        }

                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Login gagal: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
        }

        binding.tvRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

            finish()
        }
    }
}