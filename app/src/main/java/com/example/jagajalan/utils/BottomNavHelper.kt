package com.example.jagajalan.utils

import android.content.Context
import android.content.Intent
import com.example.jagajalan.R
import com.example.jagajalan.activities.CreateReportActivity
import com.example.jagajalan.activities.HistoryActivity
import com.example.jagajalan.activities.ProfileActivity
import com.example.jagajalan.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavHelper {

    fun setup(
        context: Context,
        bottomNav: BottomNavigationView,
        currentItemId: Int
    ) {
        bottomNav.selectedItemId = currentItemId

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == currentItemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.nav_home -> {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    context.startActivity(Intent(context, HistoryActivity::class.java))
                    true
                }
                R.id.nav_report -> {
                    context.startActivity(Intent(context, CreateReportActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}