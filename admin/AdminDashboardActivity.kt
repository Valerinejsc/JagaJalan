package com.example.jagajalan.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.jagajalan.R
import com.example.jagajalan.activities.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_dashboard)

        val btnManageReports =
            findViewById<Button>(R.id.btnManageReports)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        btnManageReports.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ManageReportsActivity::class.java
                )
            )
        }

        btnLogout.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }
}